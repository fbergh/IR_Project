# IR Query Expansion

Project by:<br>
[Freek van den Bergh, s4801709](https://github.com/fbergh)<br>
[Max Driessen, s4789628](https://github.com/MaxDriessen)<br>
[Marlous Nijman, s4551400](https://github.com/marlousnijman)

This project was created for the Information Retrieval course at the Radboud University.

This project requires a few things:

* All of the .jar files in the `lib` folder need to be added to the project as libraries; these are all files from Lucene 4.8.1 and one created by compiling the code found in [this repository](https://github.com/pvoosten/explicit-semantic-analysis).
* This project relies on [Lucene version 4.8.1](https://archive.apache.org/dist/lucene/java/4.8.1/) and a *termdoc* created using a tool for computing ESA (explicit semantic analysis values), which can be found in [this repository](https://github.com/pvoosten/explicit-semantic-analysis).
* The termdoc folder (building this termdoc can take more 1 hour) needs to be present in the folder in which this README is located, and needs to be named "termdoc".
* To run the code on the TREC collection, it also needs to be present in the folder where this README is located. In our research, we used `topics.robust04.txt`.
* To run the code using the Wikipedia index as opposed to the Wikipedia API, the corresponding results from the "IR Wikipedia Index" project are required (currently, these files are called `results_title.txt`, `results_body.txt`, and `query_results.txt`). These files need to be placed in the `index_results` folder.
