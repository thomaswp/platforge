package com.twp.html;

import static playn.core.PlayN.regularExpression;
import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.twp.core.Platforge;

import edu.elon.honors.price.game.Formatter;
import edu.elon.honors.price.game.Formatter.Impl;

public class PlatforgeHtml extends HtmlGame {

	@Override
	public void start() {
		Formatter.impl = new Impl() {
			@Override
			public String format(String format, Object... args) {
				final StringBuffer msg = new StringBuffer();
				int argIndex = 0;
				for (int i = 0; i < format.length(); i++) {
					if (i < format.length() - 1) {
						String sub = format.substring(i, i + 2);
						if (regularExpression().matches("%[a-z]", sub)) {
							msg.append(args[argIndex++]);
							i++;
							continue;
						}
					}
					msg.append(format.charAt(i));
				}
				return msg.toString();
			}
		};
		HtmlPlatform.Config config = new HtmlPlatform.Config();
		// use config to customize the HTML platform, if needed
		HtmlPlatform platform = HtmlPlatform.register(config);
		platform.assets().setPathPrefix("platforge/");
		PlayN.run(new Platforge());
	}
}
