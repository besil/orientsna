def lines(fname, sep=" "):
    with open(fname, 'r') as fin:
        for line in fin:
            if "%" not in line:
                yield line.strip().split(sep)

if __name__ == '__main__':
    fname = "out.ucidata-zachary"
    nodes_fname = "nodes"
    edgelist_fname = "edges"
    sep = ","

    nodes = set()
    for src, dst in lines(fname):
        nodes.add(src)
        nodes.add(dst)

    with open(nodes_fname, "w") as fout:
        for node in sorted(nodes, key=lambda x: int(x)):
            fout.write("{}\n".format(node))

    with open(edgelist_fname, "w") as fout:
        for src, dst in lines(fname):
            fout.write("{}{}{}\n".format(src, sep, dst))
