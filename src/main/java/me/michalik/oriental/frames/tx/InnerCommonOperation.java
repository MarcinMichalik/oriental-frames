package me.michalik.oriental.frames.tx;


import com.orientechnologies.orient.core.id.ORID;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.VertexFrame;

public class InnerCommonOperation extends InnerOperation {

    public InnerCommonOperation(FramedGraph<OrientGraph> framedGraph) {
        super(framedGraph);
    }

    public <T extends VertexFrame> T createVertex(Class<T> clazz){
        return this.createVertex(clazz.getSimpleName(), clazz);
    }

    public <T extends VertexFrame> T createVertex(String className, Class<T> clazz){
        return framedGraph.addVertex(className, clazz);
    }

    public <T extends EdgeFrame> T createEdge(Class<T> clazz, Vertex vertexIn, Vertex vertexOut, String label){
        return this.createEdge(clazz.getClass().getSimpleName(), clazz, vertexIn, vertexOut, label);
    }

    public <T extends EdgeFrame> T createEdge(String className, Class<T> clazz, Vertex vertexIn, Vertex vertexOut, String label){
        return framedGraph.addEdge(className, vertexOut, vertexIn, label, clazz);
    }


    public <T extends VertexFrame> void removeVertex(ORID orid, Class<T> clazz){
        T vertex = this.framedGraph.getVertex(orid, clazz);
        this.framedGraph.removeVertex(vertex.asVertex());
    }

    public <T extends EdgeFrame> void removeEdge(ORID orid, Class<T> clazz){
        T edge = this.framedGraph.getEdge(orid, clazz);
        this.framedGraph.removeEdge(edge.asEdge());
    }
}
