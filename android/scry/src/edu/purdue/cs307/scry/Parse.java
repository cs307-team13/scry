package edu.purdue.cs307.scry;

public class Parse {

public String parse(String s) {
		
		String[] tokens = s.split("[ ]+");
		String ret = "";
		for(int i = 0; i < tokens.length; i++){
			String current = tokens[i].toLowerCase();
			Verbs currentVerb;
			
			if(current.contains("ing") && current.length() > 4){
				if((current.toLowerCase()).equals("fling")){
				}
				else{
					current = current.substring(0, current.length() - 3);
				}
			}
			if(contains(current)){
				currentVerb = Verbs.valueOf(current.toLowerCase());
			}else
				continue;
			
			switch (currentVerb) {
				
				case go:
					if(tokens[i+1].equals("to"))
						ret = "Visit";
					else 
						continue;
					return ret;
				case fly:
				case driv:
				case drive:
					ret = "Visit";
				case work:
					if(tokens[i+1].equals("out"))
						ret = "Sports/Recreation";
					return ret;
				case hike:
				case hik:
				case run:
				case runn:
				case jog:
				case jogg:
				case golf:
				case play:
				case swimm:
				case swim:
					ret = "Sports/Recreation";
					return ret;
				case watch:
				case film:
					ret = "Movies/Television";
					return ret;
				case listen:
				case sing:
					ret = "Music";
					return ret;
				case read:
					ret = "Books";
					return ret;
				case hang:
					if(tokens[i+1].equals("out"))
						ret = "Social";
					return ret;
				case meet:
				case party:
					ret = "Social";
					return ret;
				case tell:
				case call:
				case talk:
				case text:
					ret = "Communication";
					return ret;
				case sell:
				case buy:
					ret = "Shopping";
					return ret;
				case drink:
				case cook:
				case eat:
					ret = "Food/Beverages";
					return ret;
				case know:
				case find:
				case learn:
					ret = "Knowledge";
					return ret;
				case pray:
					ret = "Religion";
					return ret;
				case vot:
				case vote:
					ret = "Politics";
			}
		}
		if(ret == "")
			ret = "Other";
		return ret;
	}

	public enum Verbs{
		driv,
		drive,
		go,
		golf,
		play,
		swim,
		swimm,
		watch,
		listen,
		read,
		hang,
		party,
		call,
		text,
		sell,
		buy,
		cook,
		eat,
		learn,
		know,
		fly,
		run,
		runn,
		jog,
		jogg,
		work,
		hike,
		hik,
		pray,
		vote,
		vot,
		sing,
		drink,
		find,
		talk,
		meet,
		tell,
		film
	}
	
	public static boolean contains(String test) {

	    for (Verbs v : Verbs.values()) {
	        if (v.name().equals(test)) {
	            return true;
	        }
	    }

	    return false;
	}
}
