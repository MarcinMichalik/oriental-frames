package me.michalik.oriental.frames.notx.results;


import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraph;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ResultEdgeFrameIterable<T extends EdgeFrame> {

    private final Iterable<T> edgeFrames;
    private final FramedGraph<OrientGraphNoTx> framedGraph;

    public ResultEdgeFrameIterable(Iterable<T> edgeFrames, FramedGraph<OrientGraphNoTx> framedGraph) {
        this.edgeFrames = edgeFrames;
        this.framedGraph = framedGraph;
    }

    public <R> List<R> map(Function<T, R> mapper){
        try{
            return StreamSupport.stream(this.edgeFrames.spliterator(), false).map(mapper).collect(Collectors.toList());
        }catch (Exception e){
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }
}