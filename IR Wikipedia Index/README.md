# IR Wikipedia Index

Project by:<br>
[Freek van den Bergh, s4801709](https://github.com/fbergh)<br>
[Max Driessen, s4789628](https://github.com/MaxDriessen)<br>
[Marlous Nijman, s4551400](https://github.com/marlousnijman)

This project was created for the Information Retrieval course at the Radboud University.

This project requires a few things:

* All of the .jar files in the `lib` folder need to be added to the project as libraries; these are all files from Lucene 6.2.0.
* This project relies on an index built from a Wikipedia dump, using [Lucene version 6.2.0](https://archive.apache.org/dist/lucene/java/6.2.0/) in combination with a [tool for indexing
  Wikipedia dumps](https://github.com/lemire/IndexWikipedia). Wikipedia dumps can be found [here](https://en.wikipedia.org/wiki/Wikipedia:Database_download) (use *pages-articles-multistream.xml.bz2*).
* The built index (building can take more than 24 hours) needs to be present in the folder in which this README is located, and needs to be named "wiki_index".
* To run the code on the TREC collection, it also needs to be present in the folder in which this README is located. In our research, we used `topics.robust04.txt`.
