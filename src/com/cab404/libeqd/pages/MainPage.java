package com.cab404.libeqd.pages;
import com.cab404.libeqd.data.Post;
import com.cab404.libeqd.modules.PostModule;
import com.cab404.moonlight.framework.ModularBlockParser;
import com.cab404.moonlight.framework.Page;

import java.util.ArrayList;
import java.util.List;

/**
* @author cab404
*/
public class MainPage extends Page {
	public static final int PART_POST = 0;

	public List<Post> posts = new ArrayList<>();

	@Override protected String getURL() {
		return "/";
	}

	@Override protected void bindParsers(ModularBlockParser base) {
		base.bind(new PostModule(), PART_POST);
	}

	@Override public void handle(Object object, int key) {
		switch (key) {
			case PART_POST:
				posts.add((Post) object);
				break;
		}
	}

}
