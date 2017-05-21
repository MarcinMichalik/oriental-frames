package me.michalik.oriental.frames.notx.results;

import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.VertexFrame;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ResultVertexFrameIterable<T extends VertexFrame> {

    private final Iterable<T> vertexFrames;
    private final FramedGraph<OrientGraphNoTx> framedGraph;

    public ResultVertexFrameIterable(Iterable<T> vertexFrames, FramedGraph<OrientGraphNoTx> framedGraph) {
        this.vertexFrames = vertexFrames;
        this.framedGraph = framedGraph;
    }

    public <R> List<R> map(Function<T, R> mapper){
        try{
            return StreamSupport.stream(this.vertexFrames.spliterator(), false).map(mapper).collect(Collectors.toList());
        }catch (Exception e){
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }
}