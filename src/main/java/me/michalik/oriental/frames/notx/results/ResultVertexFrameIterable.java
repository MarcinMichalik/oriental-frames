package me.michalik.oriental.frames.notx.results;

import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.VertexFrame;
import me.michalik.oriental.frames.notx.TooManyResultsException;

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

    public <R> R single(Function<T, R> mapper){
        try{
            List<T> list = StreamSupport.stream(this.vertexFrames.spliterator(), false).collect(Collectors.toList());
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