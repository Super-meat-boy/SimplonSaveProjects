package co.projetweb.application.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import co.projetweb.application.controller.annotation.ControllerMethodAnnotation;
import co.projetweb.application.model.dao.jpa.CityDAO;
import co.projetweb.application.model.dao.jpa.MonumentDAO;
import co.projetweb.application.model.dao.jpa.UserDAO;
import co.projetweb.application.model.entity.City;
import co.projetweb.application.model.entity.EntityManagerQuery;
import co.projetweb.application.model.entity.Monument;
import co.projetweb.application.model.entity.User;


public class AppController {
	
	private EntityManager em = EntityManagerQuery.getEntityManagerFactory().createEntityManager();
	
	public void preExecute() {
		System.out.println("preExecute");
		em.getTransaction().begin();
	}
	
	public void postExecute() {
		System.out.println("postExecute");
		if(em.getTransaction().isActive()) {			
			System.out.println("active");		
			em.getTransaction().commit();
		}
		else {
			System.out.println("inactive");
		}
		em.close();
	}
	
	/**
	  * Execute an action
	  * 
	  * @access public
	  * @return void 
	  * @name execute
	  * @param String : method to load 
	  * 
	  */
	
	public void execute(String methodeName) {
		Class<?> classeAInstancier = this.getClass();
		Class<?>[] types = new Class[] {};				
		try {
			Method methodePresenter = classeAInstancier.getMethod(methodeName,types);			
			this.preExecute();			
			methodePresenter.invoke(this, null);			
			this.postExecute();
			
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	  * Quit 
	  * @access public
	  * @return void 
	  * @name quit
	  * 
	  */
	
	@ControllerMethodAnnotation(name="quit",lib="Quitter",order=1)
	public void quit() {
		System.err.println("Bye !");
		System.exit(0);
	}
	
	/**
	  *  Wait
	  *
	  *  @access public
	  *  @name waitALittle
	  *  @return void
	  *  
	  */
	
	@ControllerMethodAnnotation(name="waitALittle",lib="Attendre quelques secondes",order=2)
	public void waitALittle() {
		System.out.println("Go dans : ");
		try {
			for(int i = 3 ; i > 0 ; i--) {
				System.out.print(i+" ");			
					Thread.sleep(500);
			}
			System.out.println("Go !!!");
			Thread.sleep(250);
		}
		catch (InterruptedException e) {			
			e.printStackTrace();
		}
		System.out.println();
	}
	
	/**
	  *  Affiche une ville
	  *
	  *  @access public
	  *  @name readCity
	  *  @return void
	  *  
	  */
	
	@ControllerMethodAnnotation(name="readCity",lib="Afficher une ville",order=3)
	public void readCity() {
		EntityManager em = EntityManagerQuery.getEntityManagerFactory().createEntityManager();
		CityDAO cityDAO= new CityDAO(em);
		City city = cityDAO.getById(new Long(145));
		System.out.println(city);
		
	}
	
	
	/**
	  *  Ex�cute un ensemble de Create et Update et Delete via les DAO
	  *
	  *  @access public
	  *  @name executeAllQueries
	  *  @return void
	  *  
	  */
	
	@ControllerMethodAnnotation(name="executeAllQueries",lib="Ex�cuter plusiueurs requ�te",order=4)
	public void executeAllQueries() {
		
		
		
		//Cr�ation d'une ville
		CityDAO cityDAO = new CityDAO(em);
		City city0 = new City("PARIS IDF",1,2);
		City city = cityDAO.create(city0);
				
		//R�cup�ration d'une ville via son ID
		City city2 = cityDAO.getById(city.getId());
				
		//Ici on voit que les adresses sont les m�mes donc il s'agit de 3 variables qui d�signe un seul et unique objet en m�moire
		System.out.println(city0);
		System.out.println(city);
		System.out.println(city2);
				
		//Si la ville est bien trouv�e (on ne sait jamais)
		if(city2 != null) {
			city2.setName("Montreuil IDF");
			cityDAO.update(city2);
		}
				
		//On tente de supprimer une une ville via son ID si elle existe en base
		cityDAO.deleteById(new Long(106));
		
		//On cr�er un nouveau monument dans la ville "city"
		MonumentDAO monumentDAO = new MonumentDAO(em);		
		Monument monument = new Monument("Tour Eiffel");
		city.addMonument(monument);
		
		//On cr�er un user
		UserDAO userDAO = new UserDAO(em);		
		User user = userDAO.create(new User("Xavier"));
		//Un setCity est implicite et impl�ment�e dans la classe "Monumenyt"
		user.addMonument(monument);
		
		//insert
		monumentDAO.create(monument);
		
		
		
		
	}
	
	/**
	  *  Liste les entit�s en bdd
	  *
	  *  @access public
	  *  @name list
	  *  @return void
	  *  
	  */
	
	@ControllerMethodAnnotation(name="list",lib="list Les Entites",order=5)
	public void list() {
		
		CityDAO cityDAO = new CityDAO(em);
		System.out.println(cityDAO.list());		
		
		
	}
	
	/**
	  *  Ex�cute un ensemble de Create et Update et Delete via les DAO
	  *
	  *  @access public
	  *  @name filter
	  *  @return void
	  *  
	  */
	
	@ControllerMethodAnnotation(name="filter",lib="list Les Entites en filtrant selon le name",order=6)
	public void filter() {
		
		
		CityDAO cityDAO = new CityDAO(em);
		
		System.out.println(cityDAO.filter("Mont"));		
		
		
	}
	
	/**
	  *  Liste toutes les entit�s en bdd
	  *
	  *  @access public
	  *  @name findAll
	  *  @return void
	  *  
	  */
	
	@ControllerMethodAnnotation(name="findAll",lib="Utiliser le findAll",order=7)
	public void findAll() {
		
		System.out.println(City.findAll(1, 2));
		
	}
	
	/**
	  *  Liste toutes les entit�s en bdd
	  *
	  *  @access public
	  *  @name findAll
	  *  @return void
	  *  
	  */
	
	@ControllerMethodAnnotation(name="deleteById",lib="Effacer l'id ?",order=8)
	public void deleteById() {
			
		
		try {
			City.deleteById(em, new Long(217));	
		}catch(PersistenceException e) {
			System.err.println("Pb lors de la suppression de la City demand�e !");
			em.getTransaction().rollback();
		}		
	}
	
	/**
	  *
	  * G�n�re une requete criteria
	  * 
	  */
	@ControllerMethodAnnotation(name="criteria",lib="API Criteria",order=9)
	public void criteria() {
		EntityManager em = EntityManagerQuery.getEntityManagerFactory().createEntityManager();
		/*CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<City> cq = cb.createQuery(City.class);
		Root<City> city = cq.from(City.class);
		cq.select(city);
				
		TypedQuery<City> q = em.createQuery(cq);
		System.out.println(q.getResultList());*/
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Monument> query = cb.createQuery(Monument.class);
		Root<City> city = query.from(City.class);
		Join<City, Monument> monuments = city.join("monuments");
		query.select(monuments);
		TypedQuery<Monument> q = em.createQuery(query);
		System.out.println(q.getResultList());
		
		em.close();
	}
}

