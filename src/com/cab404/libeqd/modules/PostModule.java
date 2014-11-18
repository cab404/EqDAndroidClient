package com.cab404.libeqd.modules;
import com.cab404.libeqd.data.Post;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.util.SU;

import java.util.ArrayList;

/**
 * @author cab404
 */
public class PostModule extends ModuleImpl<Post> {
    @Override public Post extractData(HTMLTree page, AccessProfile profile) {
        Post post = new Post();

		/* Only title got this address in topic block */
        Tag title = page.xPathFirstTag("div/div/h3&class=post-title*/a");

		/* Let us not underestimate the power of HTML/Internets and just scream out loud if something goes wrong */
        if (title != null) {

			/* Getting all text between given opening tag and it's closing tag */
            post.title = page.getContents(title);

			/* Getting a property from tag */
            post.link = title.get("href");

        } else
            throw new RuntimeException("I JUST DON'T KNOW WHAT WENT WRONG WITH THIS POST! \n" + page);

		/* Getting body and date*/
        post.body = page.xPathStr("div/div&itemprop=articleBody");
        post.date =
                SU.removeRecurringChars(
                        SU.removeAllTags(
                                page.xPathStr("div/div/div/span&class=post-timestamp")
                        ).replace('\n', ' ').trim(),
                        ' '
                ) + "";

		/* ALL YOUR TAGS ARE BELONG TO US */
        post.tags = new ArrayList<>();
        for (Tag tag : page.xPath("div/div/div/span&class=post-labels/a&rel=tag"))
            post.tags.add(SU.trim(page.getContents(tag)) + "");

        return post;
    }
    @Override public boolean doYouLikeIt(Tag tag) {
        return "li".equals(tag.name) && "blog-post".equals(tag.get("class"));
    }
}