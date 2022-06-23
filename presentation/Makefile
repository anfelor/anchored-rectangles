clean:
	rm -rf *.aux *.bbl *.bcf *.blg *.dvi *.fdb_latexmk *.fls *.log *.out *.run.xml *.tex.bak *.thm *.toc _minted-thesis

build:
	{ \
	pdflatex -shell-escape presentation.tex; \
	biber presentation; \
	pdflatex -shell-escape presentation.tex; \
	}