package me.michalik.oriental.frames.notx.results;

import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.VertexFrame;

import java.util.function.Function;
import java.util.function.Predicate;

public class ResultVertexFrame<T extends VertexFrame> {

    private final T vertexFrame;
    private final FramedGraph<OrientGraphNoTx> framedGraph;

    public ResultVertexFrame(T vertexFrame, FramedGraph<OrientGraphNoTx> framedGraph) {
        this.vertexFrame = vertexFrame;
        this.framedGraph = framedGraph;
    }

    public <R> R map(Function<T, R> mapper){
        try{
            return mapper.apply(this.vertexFrame);
        }catch (Exception e){
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }

    public ResultVertexFrame<T> throwIf(RuntimeException e, Predicate<T> predicate){
        if(predicate.test(this.vertexFrame)){
            return this;
        }else {
            this.framedGraph.shutdown();
            throw e;
        }
    }

    public ResultVertexFrame<T> notExistThrow(RuntimeException e){
        if(this.vertexFrame==null){
            this.framedGraph.shutdown();
            throw e;
        }else {
            return this;
        }
    }

    public void remove(){
        try{
            this.vertexFrame.asVertex().remove();
        }catch (Exception e){
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }

}
