package me.michalik.oriental.frames.tx.results;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.frames.FramedTransactionalGraph;
import com.tinkerpop.frames.VertexFrame;

import java.util.function.Function;
import java.util.function.Predicate;

public class ResultVertexFrame<T extends VertexFrame> {

    private final T vertexFrame;
    private final FramedTransactionalGraph<OrientGraph> framedGraph;

    public ResultVertexFrame(T vertexFrame, FramedTransactionalGraph<OrientGraph> framedGraph) {
        this.vertexFrame = vertexFrame;
        this.framedGraph = framedGraph;
    }

    public <R> R map(Function<T, R> mapper){
        try{
            R result = mapper.apply(this.vertexFrame);
            this.framedGraph.commit();
            return result;
        }catch (Exception e){
            this.framedGraph.rollback();
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }

    public ResultVertexFrame<T> throwIf(RuntimeException e, Predicate<T> predicate){
        if(predicate.test(this.vertexFrame)){
            return this;
        }else {
            this.framedGraph.rollback();
            this.framedGraph.shutdown();
            throw e;
        }
    }

    public ResultVertexFrame<T> notExistThrow(RuntimeException e){
        if(this.vertexFrame==null){
            this.framedGraph.rollback();
            this.framedGraph.shutdown();
            throw e;
        }else {
            return this;
        }
    }

    public void remove(){
        try{
            this.vertexFrame.asVertex().remove();
            this.framedGraph.commit();
        }catch (Exception e){
            this.framedGraph.rollback();
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }

}
