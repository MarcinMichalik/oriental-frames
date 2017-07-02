package me.michalik.oriental.frames.tx;


import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.sql.query.OSQLQuery;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedTransactionalGraph;
import com.tinkerpop.frames.VertexFrame;
import me.michalik.oriental.frames.tx.results.ResultEdgeFrame;
import me.michalik.oriental.frames.tx.results.ResultEdgeFrameIterable;
import me.michalik.oriental.frames.tx.results.ResultVertexFrame;
import me.michalik.oriental.frames.tx.results.ResultVertexFrameIterable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FramedTransactionalGraphOperation {

    private final FramedTransactionalGraph<OrientGraph> framedGraph;

    public FramedTransactionalGraphOperation(FramedTransactionalGraph<OrientGraph> framedGraph) {
        this.framedGraph = framedGraph;
    }

    public void frame(Consumer<FramedTransactionalGraph> consumer){
        try{
            consumer.accept(this.framedGraph);
            this.framedGraph.commit();
        }catch (Exception e){
            this.framedGraph.rollback();
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }

    public void frame(BiConsumer<FramedTransactionalGraph, InnerCommonOperation> consumer){
        try{
            consumer.accept(this.framedGraph, new InnerCommonOperation(this.framedGraph));
        }catch (Exception e){
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }

    public <T extends VertexFrame> ResultVertexFrame<T> findVertexById(ORID orid, Class<T> clazz){
        return new ResultVertexFrame<>(clazz.cast(this.framedGraph.getVertex(orid, clazz)), this.framedGraph);
    }

    public <T extends EdgeFrame> ResultEdgeFrame<T> findEdgeById(ORID orid, Class<T> clazz){
        return new ResultEdgeFrame<>(clazz.cast(this.framedGraph.getEdge(orid, clazz)), this.framedGraph);
    }

    public <T extends VertexFrame> ResultVertexFrameIterable<T> queryVertex(String query, Class<T> clazz, Object... objects){
        return this.queryVertex(new OSQLSynchQuery(query), clazz, objects);
    }

    public <T extends VertexFrame> ResultVertexFrameIterable<T> queryVertex(OSQLQuery query, Class<T> clazz, Object... objects){
        Iterable<T> vertexFrames = this.framedGraph.frameVertices(framedGraph.getBaseGraph().command(query).execute(objects), clazz);
        return new ResultVertexFrameIterable<>(vertexFrames, this.framedGraph);
    }

    public <T extends EdgeFrame> ResultEdgeFrameIterable<T> queryEdge(String query, Class<T> clazz, Object... objects){
        return this.queryEdge(new OSQLSynchQuery(query), clazz, objects);
    }

    public <T extends EdgeFrame> ResultEdgeFrameIterable<T> queryEdge(OSQLQuery query, Class<T> clazz, Object... objects){
        Iterable<T> edgeFrames = this.framedGraph.frameEdges(framedGraph.getBaseGraph().command(query).execute(objects), clazz);
        return new ResultEdgeFrameIterable<>(edgeFrames, this.framedGraph);
    }

    public <T extends VertexFrame> Object saveVertex(Class<T> clazz, Consumer<T> consumer){
        return this.saveVertex(clazz.getSimpleName(), clazz, consumer);
    }

    public <T extends VertexFrame> Object saveVertex(String className, Class<T> clazz, Consumer<T> consumer){
        try{
            T frameVertex =  this.framedGraph.addVertex("class:" + className, clazz);
            consumer.accept(clazz.cast(frameVertex));
            this.framedGraph.commit();
            return frameVertex.asVertex().getId();
        }catch (Exception e){
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }

    public <T extends VertexFrame> Object saveVertex(Class<T> clazz, BiConsumer<T, InnerOperation> consumer){
        return this.saveVertex(clazz.getSimpleName(), clazz, consumer);
    }

    public <T extends VertexFrame> Object saveVertex(String className, Class<T> clazz, BiConsumer<T, InnerOperation> consumer){
        try{
            T frameVertex =  this.framedGraph.addVertex("class:" + className, clazz);
            consumer.accept(clazz.cast(frameVertex), new InnerOperation(this.framedGraph));
            this.framedGraph.commit();
            return frameVertex.asVertex().getId();
        }catch (Exception e){
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }

//    public <T extends EdgeFrame> Object saveEdge(Class<T> clazz, Consumer<T> consumer){
//        return this.saveEdge(clazz.getClass().getSimpleName(), clazz, consumer);
//    }
//
//    public <T extends EdgeFrame> Object saveEdge(String className, Class<T> clazz, Consumer<T> consumer){
//        try{
//            T edgeFrame =  this.framedGraph.addVertex("class:" + className, clazz);
//            consumer.accept(clazz.cast(edgeFrame));
//            this.framedGraph.commit();
//            return edgeFrame.asEdge().getId();
//        }catch (Exception e){
//            throw e;
//        }finally {
//            this.framedGraph.shutdown();
//        }
//    }

    public <T extends VertexFrame> void updateVertex(ORID orid, Class<T> clazz, Consumer<T> consumer){
        try{
            T vertexFrame = this.framedGraph.getVertex(orid, clazz);
            consumer.accept(vertexFrame);
            this.framedGraph.commit();
        }catch (Exception e){
            this.framedGraph.rollback();
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }

    public <T extends VertexFrame> void updateVertex(ORID orid, Class<T> clazz, BiConsumer<T, InnerOperation> consumer){
        try{
            T vertexFrame = this.framedGraph.getVertex(orid, clazz);
            consumer.accept(vertexFrame, new InnerOperation(this.framedGraph));
            this.framedGraph.commit();
        }catch (Exception e){
            this.framedGraph.rollback();
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }

    public <T extends EdgeFrame> void updateEdge(ORID orid, Class<T> clazz, Consumer<T> consumer){
        try{
            T edgeFrame = this.framedGraph.getEdge(orid, clazz);
            consumer.accept(edgeFrame);
            this.framedGraph.commit();
        }catch (Exception e){
            this.framedGraph.rollback();
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }

    public <T extends EdgeFrame> void updateEdge(ORID orid, Class<T> clazz, BiConsumer<T, InnerOperation> consumer){
        try{
            T edgeFrame = this.framedGraph.getEdge(orid, clazz);
            consumer.accept(edgeFrame, new InnerOperation(this.framedGraph));
            this.framedGraph.commit();
        }catch (Exception e){
            this.framedGraph.rollback();
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }

    public <T extends VertexFrame> void removeVertex(ORID orid, Class<T> clazz){
        try{
            T vertex = this.framedGraph.getVertex(orid, clazz);
            this.framedGraph.removeVertex(vertex.asVertex());
        }catch (Exception e){
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }

    public <T extends EdgeFrame> void removeEdge(ORID orid, Class<T> clazz){
        try{
            T edge = this.framedGraph.getEdge(orid, clazz);
            this.framedGraph.removeEdge(edge.asEdge());
        }catch (Exception e){
            throw e;
        }finally {
            this.framedGraph.shutdown();
        }
    }
}