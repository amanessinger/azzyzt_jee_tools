USING_HTML = using_azzyzt.html
HTML = changelog.html readme.html $(USING_HTML) developing_azzyzt.html
TUTORIAL = tutor
README  = ../README.txt

%.html : %.viki
	deplate -X -m code-highlight -o $@ $<
	head -n -3 $@ > xxx.tmp
	cat xxx.tmp statcounter.inc tail.inc > $@
	rm -f xxx.tmp

all:    $(HTML) toc $(README) clean

.PHONY: toc
toc:
	./bin/insert_toc.sh $(USING_HTML)

$(README) : readme.viki
	deplate -f plain -o $@ $<

.PHONY: clean veryclean
clean:
	rm -f *_*[0-9].html *.XML *.dot code_* highlight.css *~

veryclean: clean
	rm -f $(HTML)
