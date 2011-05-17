HTML = readme.html using_azzyzt.html
README  = ../README.txt

%.html : %.viki
	deplate -X -m code-highlight -o $@ $<

all:    $(HTML) $(README) clean

$(README) : readme.viki
	deplate -f plain -o $@ $<

clean:
	rm -f *_*[0-9].html *.XML code_* highlight.css *~
