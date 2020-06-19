package it.polito.tdp.food.model;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDAO;

public class Model {
	
	FoodDAO dao = new FoodDAO();
	Graph<Condiment, DefaultWeightedEdge> grafo;
	
	public Graph<Condiment, DefaultWeightedEdge> creaGrafo(double calorie) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.vertici(calorie));
		for(Adiacenza a : dao.archi(calorie)) {
			if(grafo.vertexSet().contains(a.getC1()) && grafo.vertexSet().contains(a.getC2()))
				Graphs.addEdge(grafo, a.getC1(), a.getC2(), a.getPeso());
		}
		System.out.println(grafo);
		return grafo;
	}

	public int tot(double calorie, int id){
		return dao.tot(calorie, id);
	}
}
