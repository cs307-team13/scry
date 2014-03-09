package edu.purdue.cs307.scry;

public class Parse {

public String parse(String s) {
		
		String[] tokens = s.split("[ ]+");
		String ret = null;
		for(int i = 0; i < tokens.length; i++){
			String current = tokens[i];
			Verbs currentVerb;
			if(contains(current)){
				currentVerb = Verbs.valueOf(current.toLowerCase());
			}else
				continue;
			
			switch (currentVerb) {
				
				case go:
					if(tokens[i+1].equals("to"))
						ret = "Visit";
					break;
				case golf:
				case play:
				case swim:
					ret = "Games/Sports";
					break;
				case watch:
					ret = "Movies/Television";
					break;
				case listen:
					ret = "Music";
					break;
				case read:
					ret = "Books";
					break;
				case hang:
					if(tokens[i+1].equals("out"))
						ret = "Social";
					break;
				case party:
					ret = "Social";
					break;
				case call:
				case text:
					ret = "Communication";
					break;
				case sell:
				case buy:
					ret = "Shopping";
					break;
				case cook:
				case eat:
					ret = "Food";
					break;
				default:
					ret = "Other";
					break;
			}
			if(!ret.equals("Other"))
				break;
		}
		return ret;
	}

	public enum Verbs{
		go,
		golf,
		play,
		swim,
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
		eat
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
