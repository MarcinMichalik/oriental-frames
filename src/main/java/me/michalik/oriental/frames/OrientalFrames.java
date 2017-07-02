package me.michalik.oriental.frames;


import com.tinkerpop.frames.FramedGraphFactory;
import me.michalik.oriental.core.Oriental;
import me.michalik.oriental.core.OrientalProperties;
import me.michalik.oriental.frames.notx.FramedGraphOperation;
import me.michalik.oriental.frames.tx.FramedTransactionalGraphOperation;

public class OrientalFrames extends Oriental{

    private final FramedGraphFactory framedGraphFactory;

    public OrientalFrames(OrientalProperties orientalProperties, FramedGraphFactory framedGraphFactory) {
        super(orientalProperties);
        this.framedGraphFactory = framedGraphFactory;
    }

    public FramedGraphOperation framedGraphNoTxOperation(){
        return new FramedGraphOperation(this.framedGraphFactory.create(super.getOrientGraphNoTx()));
    }

    public FramedTransactionalGraphOperation framedGraphOperation(){
        return new FramedTransactionalGraphOperation(this.framedGraphFactory.create(super.getOrientGraph()));
    }
}
