package me.michalik.oriental.frames.notx.results;


import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraph;

import java.util.function.Function;
import java.util.function.Predicate;

public class ResultEdgeFrame<T extends EdgeFrame> {

    private final T edgeFrame;
    private final FramedGraph<OrientGraphNoTx> framedGraph;

    public ResultEdgeFrame(T edgeFrame, FramedGraph<OrientGraphNoTx> framedGraph) {
        this.edgeFrame = edgeFrame;
        this.framedGraph = framedGraph;
    }

    public <R> R map(Function<T, R> mapper){
        try {
            return mapper.apply(this.edgeFrame);
        }catch (Exception e){
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }

    public ResultEdgeFrame<T> throwIf(RuntimeException e, Predicate<T> predicate){
        if(predicate.test(this.edgeFrame)){
            return this;
        }else {
            this.framedGraph.shutdown();
            throw e;
        }
    }

    public ResultEdgeFrame<T> notExistThrow(RuntimeException e){
        if(this.edgeFrame==null){
            this.framedGraph.shutdown();
            throw e;
        }else {
            return this;
        }
    }

    public void remove(){
        try{
            this.edgeFrame.asEdge().remove();
        }catch (Exception e){
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }
}
