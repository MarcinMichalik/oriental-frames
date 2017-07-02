package me.michalik.oriental.frames.tx;


import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.sql.query.OSQLQuery;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.VertexFrame;

public class InnerOperation {

    protected final FramedGraph<OrientGraph> framedGraph;

    public InnerOperation(FramedGraph<OrientGraph> framedGraph) {
        this.framedGraph = framedGraph;
    }


    public <T extends VertexFrame> T findVertexById(ORID orid, Class<T> clazz){
        return this.framedGraph.getVertex(orid, clazz);
    }

    public <T extends EdgeFrame> T findEdgeById(ORID orid, Class<T> clazz){
        return this.framedGraph.getEdge(orid, clazz);
    }

    public <T extends VertexFrame> Iterable<T> queryVertex(String query, Class<T> clazz, Object... objects){
        return this.queryVertex(new OSQLSynchQuery(query), clazz, objects);
    }

    public <T extends VertexFrame> Iterable<T> queryVertex(OSQLQuery query, Class<T> clazz, Object... objects){
        return this.framedGraph.frameVertices(framedGraph.getBaseGraph().command(query).execute(objects), clazz);
    }

    public <T extends EdgeFrame> Iterable<T> queryEdge(String query, Class<T> clazz, Object... objects){
        return this.queryEdge(new OSQLSynchQuery(query), clazz, objects);
    }

    public <T extends EdgeFrame> Iterable<T> queryEdge(OSQLQuery query, Class<T> clazz, Object... objects){
        return this.framedGraph.frameEdges(framedGraph.getBaseGraph().command(query).execute(objects), clazz);
    }
}