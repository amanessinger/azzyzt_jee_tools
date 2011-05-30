HTML = changelog.html readme.html using_azzyzt.html developing_azzyzt.html
README  = ../README.txt

%.html : %.viki
	deplate -X -m code-highlight -o $@ $<
	head -n -3 $@ > xxx.tmp
	cat xxx.tmp statcounter.inc tail.inc > $@
	rm -f xxx.tmp

all:    $(HTML) $(README) clean

$(README) : readme.viki
	deplate -f plain -o $@ $<

clean:
	rm -f *_*[0-9].html *.XML code_* highlight.css *~
