package k_gen_algo_ackley;

import java.util.Random;

public class Gen_Ackley_no_code {
	
	public static Random rng = new Random();
	
	public static void main(String[] args){
		int count_candidate_ncs = 100;
		int dim = 6;
		candidate_nc best_fitness = new candidate_nc(dim);

		
		candidate_nc_container_nc x = new candidate_nc_container_nc(dim, count_candidate_ncs);
		for(int i = 0; i < 10000; i++){
			x.calc_survivability();
			for(candidate_nc c : x.p){
				System.out.print(c.survivability+"("+c.ackley()+ ")" + ";");
			
			}
			System.out.println(" ");	
			x.selection();
			x.recombination();
			x.mutation();
			if(x.best_fitness().fitness() > best_fitness.fitness()) best_fitness = x.best_fitness();
		}
		
		System.out.println(best_fitness);	
	}
	
}

class candidate_nc_container_nc{
	public candidate_nc[] p;
	public int dim;
	public int count;
	
	public candidate_nc_container_nc(int dim, int count){
		this.dim = dim;
		this.count = count;
		p = new candidate_nc[count];
		for(int i = 0; i < p.length; i++){
			p[i] = new candidate_nc(dim);
		}
	}
	public candidate_nc best_fitness(){
		candidate_nc bf = p[0];
		for(candidate_nc c : p){
			if(bf.fitness() < c.fitness())bf = c;
		}
		candidate_nc x = new candidate_nc(bf.val);
		return x;
	}
	
	public void mutation(){
		for(int i = 0; i < p.length; i++ ){
			for(int j = 0; j < p[0].dim; j++){
				int x = Gen_Ackley_no_code.rng.nextInt(this.dim);
				if(x == 5){
					p[i].val[j] = Gen_Ackley_no_code.rng.nextDouble() * 80 - 40;
				}
			}
		}
	}
	
	public void recombination(){
		candidate_nc[] x = new candidate_nc[p.length];
		for(int i = 0; i < p.length; i++){
			int rnd = Gen_Ackley_no_code.rng.nextInt(p.length);
			x[i] = p[i].recombineOnePointCross(p[rnd]);
		}
		this.p = x;
	}
	
	public void selection(){
		candidate_nc[] selected_population = new candidate_nc[p.length];
		for(int i = 0; i < p.length; i++){
			double rnd = Gen_Ackley_no_code.rng.nextDouble();
			double selector = 0;
			for(int j = 0; j < p.length; j++){
				if(rnd <= selector || selector >= 1){
					selected_population[i] = new candidate_nc(p[j].val);
					selector = 0;
					break;
				}
				else{ 
					selector += p[j].survivability;
					
				}
				if(selector >= 0.98) selected_population[i] = new candidate_nc(p[p.length-1].val);
			}
			
		}
		this.p = selected_population.clone();
	}
	
	@Override
	public candidate_nc_container_nc clone(){
		candidate_nc_container_nc x = new candidate_nc_container_nc(this.dim, this.count);
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
		for(candidate_nc c : p){
			c.survivability = c.fitness()/sum_fitnesses;
		}
	}
}


class candidate_nc {
	public int xmin = -40;
	public int xmax = 40;
	public double[] val;
	public int dim;
	public double fitness;
	public double survivability;
	
	public candidate_nc(double[] val){
		this.val = new double[val.length];
		for(int i = 0; i < val.length; i++){
			this.val[i] = val[i];
		}
		this.dim = val.length;
		this.fitness = this.fitness();
	}
	
	public candidate_nc(int dim){
		double[] val = new double[dim];
		
		for(int i = 0; i < dim; i++){
			val[i] = Gen_Ackley_no_code.rng.nextDouble() * 80 - 40;
		}
		
		this.val = val;
		this.dim = dim;
		this.fitness = this.fitness();
	}
	public void copy_vals(candidate_nc copy_to){
		for(int i = 0; i < dim; i++){
			copy_to.val[i] = this.val[i];
		}
	}
	public static candidate_nc getNearZeroConfig(){
		
    return new candidate_nc( new double[] { 0.01, 0.01, 0.01, 0.01, 0.01, 0.01 });
	
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
		return -20.0*Math.exp(-0.2*Math.sqrt(sum1 / ((double )val.length)))  
				- Math.exp(sum2 /((double )val.length))+ 20 + Math.exp(1.0);
	}
	
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < this.dim; i++){
			sb.append("x"+(i+1)+"["+this.val[i]+"]"+'\n');
		}
		
		return sb.toString();
	}
	
	public candidate_nc recombineOnePointCross(candidate_nc parent)
	{
		double[] valChild = new double[6];
		valChild[0] = val[0];
		valChild[1] = val[1];
		valChild[2] = val[2];
		valChild[3] = parent.val[3];
		valChild[4] = parent.val[4];
		valChild[5] = parent.val[5];
		candidate_nc child = new candidate_nc(valChild);
		return child;
	}
}
	
	