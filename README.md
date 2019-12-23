# Information Retrieval Project

## About

This repository contains the code of our project for the NWI-I00041 Information Retrieval course at Radboud University (2019-2020).

**Abstract:** <br>
In this paper, we will investigate whether Wikipedia-based query expansion can be used to improve information retrieval systems. Our approach uses the Wikipedia and DBpedia knowledge bases to generate possible expansion candidates. These expansion candidates are then ranked using Explicit Semantic Analysis (ESA), and the top candidates are selected to expand the query. We conducted several experiments to compare different methods of querying Wikipedia, different numbers of expansion candidates, and different filtering methods. Unfortunately, the results showed that none of our implementations outperformed the baseline information retrieval system.

## Structure

The code has been organised as follows:

* `IR Query Expansion` is a Java (NetBeans) project that contains the code for query expansion. Further details about the requirements can be found in the `README.txt` in the project.
* `IR Wikipedia Index` is a Java (NetBeans) project that contains the code for retrieving query-expansion-candidates from Wikipedia. This project was not integrated with `IR Query Expansion`, because the projects require different Lucene versions which causes conflicts. Further details about the requirements can be found in the `README.txt` in the project.
* `experiment` is a directory that contains the expanded queries we used, the evaluation metrics we obtained, and the plots we generated. Furthermore, it contains two Python files `evaluate_experiment.py` and `generate_plots.py`. The former runs our experiments to obtain the evaluation metrics and the latter generates the interpolated precision-recall plots from the obtained evaluation metrics.

## Contributors

[Freek van den Bergh](https://github.com/fbergh)<br>
[Max Driessen](https://github.com/MaxDriessen)<br>
[Marlous Nijman](https://github.com/marlousnijman)
