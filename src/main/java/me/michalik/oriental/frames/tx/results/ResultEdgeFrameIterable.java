package me.michalik.oriental.frames.tx.results;


import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedTransactionalGraph;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ResultEdgeFrameIterable<T extends EdgeFrame> {

    private final Iterable<T> edgeFrames;
    private final FramedTransactionalGraph<OrientGraph> framedGraph;

    public ResultEdgeFrameIterable(Iterable<T> edgeFrames, FramedTransactionalGraph<OrientGraph> framedGraph) {
        this.edgeFrames = edgeFrames;
        this.framedGraph = framedGraph;
    }

    public <R> List<R> map(Function<T, R> mapper){
        try{
            List<R> result = StreamSupport.stream(this.edgeFrames.spliterator(), false).map(mapper).collect(Collectors.toList());
            this.framedGraph.commit();
            return result;
        }catch (Exception e){
            this.framedGraph.rollback();
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }
}