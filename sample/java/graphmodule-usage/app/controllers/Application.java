package controllers;

import de.tfelix.play.opengraph.Opengraph;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

	private static long visits = 0;

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	public static Result page() {
		// Example of binding a variable value to a Facebook tag. For example if you have a permanent
		// URL and want to give each page a unique description. Note: This example is a bit flawed since a fast
		// changing value like visitor count will not be picked up the the Facebook scraper. It is just included
		// to show you the process of dynamic parameter setting.
		Opengraph.bindArgs("/page", "og:description", visits);
		visits++;
		return ok(page.render("Second example page."));
	}
}