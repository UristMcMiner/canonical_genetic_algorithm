package k_gen_algo_ackley;

import java.util.Random;

public class Gen_Ackley {
	
	public static Random rng = new Random();
	
	public static void main(String[] args){
		int count_candidates = 100;
		int dim = 6;
		candidate best_fitness = new candidate(dim);

		
		candidate_container x = new candidate_container(dim, count_candidates);
		for(int i = 0; i < 1000000; i++){
			x.calc_survivability();
			for(candidate c : x.p){
				//System.out.print(c.survivability+"("+c.ackley()+")" + ";");
			
			}
			//System.out.println(" ");	
			x.selection();
			x.mutation();
			if(x.best_fitness().fitness() > best_fitness.fitness()) best_fitness = x.best_fitness();
		}
		
		System.out.println(best_fitness);
		
		
		
		
	}
	
}

class candidate_container{
	public candidate[] p;
	public int dim;
	public int count;
	
	public candidate_container(int dim, int count){
		this.dim = dim;
		this.count = count;
		p = new candidate[count];
		for(int i = 0; i < p.length; i++){
			p[i] = new candidate(dim);
		}
	}
	public candidate best_fitness(){
		candidate bf = p[0];
		for(candidate c : p){
			if(bf.fitness() < c.fitness())bf = c;
		}
		candidate x = new candidate(bf.val);
		return x;
	}
	
	public void mutation(){
		for(int i = 0; i < p.length; i++ ){
			for(int j = 0; j < p[0].dim; j++){
				int x = Gen_Ackley.rng.nextInt(this.dim);
				if(x == 5){
					p[i].val[j] = Gen_Ackley.rng.nextDouble() * 80 - 40;
				}
			}
		}
	}
	
	public void selection(){
		candidate[] selected_population = new candidate[p.length];
		for(int i = 0; i < p.length; i++){
			double rnd = Gen_Ackley.rng.nextDouble();
			double selector = 0;
			for(int j = 0; j < p.length; j++){
				if(rnd <= selector || selector >= 1){
					selected_population[i] = new candidate(p[j].val);
					selector = 0;
					break;
				}
				else{ 
					selector += p[j].survivability;
					
				}
				if(selector >= 0.98) selected_population[i] = new candidate(p[p.length-1].val);
			}
			
		}
		this.p = selected_population.clone();
	}
	
	@Override
	public candidate_container clone(){
		candidate_container x = new candidate_container(this.dim, this.count);
		for(int i = 0; i < p.length; i++){
			this.p[i].copy_vals(x.p[i]);
		}
		return x;
	}
	
	public void calc_survivability(){
		double sum_fitnesses = 0;
		for(int i = 0; i < p.length; i++){
			sum_fitnesses += p[i].fitness();
		}
		for(candidate c : p){
			c.survivability = c.fitness()/sum_fitnesses;
		}
	}
}


class candidate {
	public double[] val;
	public int dim;
	public double fitness;
	public double survivability;
	
	public candidate(double[] val){
		this.val = new double[val.length];
		for(int i = 0; i < val.length; i++){
			this.val[i] = val[i];
		}
		this.dim = val.length;
		this.fitness = this.fitness();
	}
	
	public candidate(int dim){
		double[] val = new double[dim];
		
		for(int i = 0; i < dim; i++){
			val[i] = Gen_Ackley.rng.nextDouble() * 80 - 40;
		}
		
		this.val = val;
		this.dim = dim;
		this.fitness = this.fitness();
	}
	public void copy_vals(candidate copy_to){
		for(int i = 0; i < dim; i++){
			copy_to.val[i] = this.val[i];
		}
	}
	public static candidate getNearZeroConfig(){
		
    return new candidate( new double[] { 0.01, 0.01, 0.01, 0.01, 0.01, 0.01 });
	
	}
	public double fitness(){
		double x = (1 - ackley()/30) * 1000;
		if(x < 0) x = 0;
		if(x > 1000) x = 1000;
		return x;
	}
	
	public double ackley(){
		double sum1 = 0.0;
		double sum2 = 0.0;
		
		for (int i = 0 ; i < this.val.length ; i ++) {
			sum1 += Math.pow(this.val[i], 2);
			sum2 += (Math.cos(2*Math.PI*this.val[i]));
		}
		return -20.0*Math.exp(-0.2*Math.sqrt(sum1 / ((double )this.val.length)))  
				- Math.exp(sum2 /((double )this.val.length))+ 20 + Math.exp(1.0);
	}
	
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < this.dim; i++){
			sb.append("x"+(i+1)+"["+this.val[i]+"]"+'\n');
		}
		
		return sb.toString();
	}
}