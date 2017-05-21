package me.michalik.oriental.frames.tx;


import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.frames.FramedGraph;

public class FramedGraphOperation {

    private final FramedGraph<OrientGraph> framedGraph;

    public FramedGraphOperation(FramedGraph<OrientGraph> framedGraph) {
        this.framedGraph = framedGraph;
    }
}