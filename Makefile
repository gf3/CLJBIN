CSSDIR := $(shell pwd)/resources/public/css
JSDIR := $(shell pwd)/resources/public/js

css:
	cat \
	  $(CSSDIR)/normalize.css \
	  $(CSSDIR)/cljbin.css \
	  $(CSSDIR)/media_queries.css \
	  $(CSSDIR)/font-awesome.css \
	  $(CSSDIR)/shThemeTomorrow.css \
	  $(CSSDIR)/shClojureExtra.css \
	  > $(CSSDIR)/compiled/all.css
	cleancss -o $(CSSDIR)/compiled/all.css $(CSSDIR)/compiled/all.css

js:
	cat \
	  $(JSDIR)/vendor/shCore.js \
	  $(JSDIR)/vendor/shBrushClojure.js \
	  $(JSDIR)/main.js \
	  > $(JSDIR)/compiled/all.js
	uglifyjs -nc -o $(JSDIR)/compiled/all.js $(JSDIR)/compiled/all.js

minify: css js

.PHONY: css js minify
