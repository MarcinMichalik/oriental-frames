package me.michalik.oriental.frames.tx.results;


import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedTransactionalGraph;
import me.michalik.oriental.frames.notx.TooManyResultsException;

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

    public <R> R single(Function<T, R> mapper){
        try{
            List<T> list = StreamSupport.stream(this.edgeFrames.spliterator(), false).collect(Collectors.toList());
            if(list.size()>1){
                throw new TooManyResultsException("Expected one result (or null) to be returned by single(), but found: " + list.size());
            }else if(list.size()==1){
                return mapper.apply(list.get(0));
            }else{
                return null;
            }
        }catch (Exception e){
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }
}