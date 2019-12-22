import matplotlib.pyplot as plt
import numpy as np

recall_vals = np.arange(1.1,step=0.1,)

def extract_iprec_vals(experiment, values, plot_baseline=False):
    iprec_vals = {}
    if plot_baseline:
        values.append("baseline")

    for val in values:
        cur_iprec_vals = []
        filename = "baseline.log" if val=="baseline" else "experiment_"+experiment+"="+str(val)+".log"
        with open("output/results/"+filename, "r") as log:
            cur_line = log.readline()
            while(not cur_line.startswith("iprec")):
                cur_line = log.readline()

            while(cur_line.startswith("iprec")):
                cur_iprec_vals.append(float(cur_line[-6:]))
                cur_line = log.readline()
            log.close()
        iprec_vals[val] = cur_iprec_vals

    return iprec_vals

def generate_iprec_plot(experiment, values, title, plot_baseline=False):
    exp_iprec_vals = extract_iprec_vals(experiment, values, plot_baseline)
    
    for val in values:
        if val=="baseline":
            plt.plot(recall_vals, exp_iprec_vals["baseline"], label="baseline")
        else:
            plt.plot(recall_vals,exp_iprec_vals[val],label=experiment+"="+str(val))
    plt.xlabel("Recall")
    plt.ylabel("Precision")
    plt.title(title)
    plt.legend()
    plt.grid(True)
    plt.savefig("output/results/plots/experiment_"+experiment+"_plot.png")
    plt.show()

generate_iprec_plot("k",[1,3,5,10], "Interpolated recall-precision for different values of K")
generate_iprec_plot("filtermethod", ["duplicates","stopwords","both"], "Interpolated recall-precision for different filter methods")
# generate_iprec_plot("method",["api","index"], "Interpolated recall-precision for different Wikipedia-query methods", True)