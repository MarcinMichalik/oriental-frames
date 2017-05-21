package me.michalik.oriental.frames;


import com.tinkerpop.frames.FramedGraphFactory;
import me.michalik.oriental.core.Oriental;
import me.michalik.oriental.core.OrientalProperties;
import me.michalik.oriental.frames.notx.FramedGraphNoTxOperation;
import me.michalik.oriental.frames.tx.FramedGraphOperation;

public class OrientalFrames extends Oriental{

    private final FramedGraphFactory framedGraphFactory;

    public OrientalFrames(OrientalProperties orientalProperties, FramedGraphFactory framedGraphFactory) {
        super(orientalProperties);
        this.framedGraphFactory = framedGraphFactory;
    }

    public FramedGraphNoTxOperation framedGraphNoTxOperation(){
        return new FramedGraphNoTxOperation(this.framedGraphFactory.create(getOrientGraphNoTx()));
    }

    public FramedGraphOperation framedGraphOperation(){
        return new FramedGraphOperation(this.framedGraphFactory.create(getOrientGraph()));
    }
}
