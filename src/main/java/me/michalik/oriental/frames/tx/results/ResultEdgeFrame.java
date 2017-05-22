package me.michalik.oriental.frames.tx.results;


import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedTransactionalGraph;

import java.util.function.Function;
import java.util.function.Predicate;

public class ResultEdgeFrame<T extends EdgeFrame> {

    private final T edgeFrame;
    private final FramedTransactionalGraph<OrientGraph> framedGraph;

    public ResultEdgeFrame(T edgeFrame, FramedTransactionalGraph<OrientGraph> framedGraph) {
        this.edgeFrame = edgeFrame;
        this.framedGraph = framedGraph;
    }

    public <R> R map(Function<T, R> mapper){
        try {
            R result = mapper.apply(this.edgeFrame);
            this.framedGraph.commit();
            return result;
        }catch (Exception e){
            this.framedGraph.rollback();
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }

    public ResultEdgeFrame<T> throwIf(RuntimeException e, Predicate<T> predicate){
        if(predicate.test(this.edgeFrame)){
            return this;
        }else {
            this.framedGraph.rollback();
            this.framedGraph.shutdown();
            throw e;
        }
    }

    public ResultEdgeFrame<T> notExistThrow(RuntimeException e){
        if(this.edgeFrame==null){
            this.framedGraph.rollback();
            this.framedGraph.shutdown();
            throw e;
        }else {
            return this;
        }
    }

    public void remove(){
        try{
            this.edgeFrame.asEdge().remove();
            this.framedGraph.commit();
        }catch (Exception e){
            this.framedGraph.rollback();
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }
}
