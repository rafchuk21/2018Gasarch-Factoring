colNames <- c("N", "least factor", "time", "margin", "sample")

data <- data.frame(matrix(ncol = 7, nrow = 0))
colnames(data) <- c(colNames, "bits", "method")

for (i in 8:28) {
  temp <- read.csv(paste("~/GitHub/2018Gasarch-Factoring/io/FactoringTimes/Home/DiffSquares", i, "Bit.txt", sep=""), header=FALSE)
  colnames(temp) <- colNames
  temp$bits = i
  temp$method = "Difference of Squares"
  data <- rbind(data, temp)
}

for (i in c(8:28, seq(30, 48, 2), seq(52, 56, 4))) {
  temp <- read.csv(paste("~/GitHub/2018Gasarch-Factoring/io/FactoringTimes/Home/PollardRho", i, "Bit.txt", sep=""), header=FALSE)
  colnames(temp) <- colNames
  temp$bits = i
  temp$method = "Pollard Rho"
  data <- rbind(data, temp)
}

for (i in c(9:28, seq(30, 48, 2), seq(52, 52, 4))) {
  temp <- read.csv(paste("~/GitHub/2018Gasarch-Factoring/io/FactoringTimes/Home/QuadraticSieve", i, "Bit.txt", sep=""), header=FALSE)
  colnames(temp) <- colNames
  temp$bits = i
  temp$method = "Quadratic Sieve"
  data <- rbind(data, temp)
}

DiffSquares <- subset(data, method == "Difference of Squares")
Pollard <- subset(data, method == "Pollard Rho")
QuadraticSieve <- subset(data, method == "Quadratic Sieve")