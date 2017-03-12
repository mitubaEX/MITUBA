
// input -> birthmark, csv, csv

extractor = bmsys.extractor("uc");
source1 = fs.open(argv[1]);
source2 = fs.open(argv[2]);
p = extractor.extract(source1);
q = extractor.extract(source2);

pair2 = bmsys.pairMaker("RoundRobin")
// pair2 = bmsys.pairMaker("RoundRobin")
// comparator = bmsys.comparator("JaccardIndex")
// comparator = bmsys.comparator("DiceIndex")
comparator = bmsys.comparator("SimpsonIndex")

birthmarks = p.merge(q);

comparisons = comparator.compare(birthmarks, pair2);

fs.print(comparisons);

// fs.print("extraction: " + birthmarks.time() + " ns")
// fs.print("comparison: " + comparisons.time() + " ns")
fs.print("" + comparisons.time() + "")
