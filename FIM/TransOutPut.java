package FIM;

import Controller.DbController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by chenhao on 5/28/16.
 */

//输出一系列 Transaction 文件
public class TransOutPut {
    public static void main(String args[]) {
        DbController dbController = new DbController();
        FimController fimController = new FimController(dbController);
        fimController.loadClusterMapFromLocalDb();
        int mapSize = fimController.candidateClusterMap.size();
        for (int clusterId = 1; clusterId <= mapSize; clusterId++) {
            fimController.buildAppReviewerMap(clusterId);
            Map appReviewerMap = fimController.appReviewerMap;
            Map reviewerAppMap = fimController.reviewerAppMap;
            try {
                TransOutPut transOutPut = new TransOutPut();
                transOutPut.BufferedWriterTest(appReviewerMap, reviewerAppMap, clusterId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void BufferedWriterTest(Map<String, TreeSet<String>> appReviewerMap, Map<String, Set<String>> reviewerAppMap, int clusterId) throws IOException {
        String x = "source/result%d.txt";
        String filename = String.format(x, clusterId);
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        for (Map.Entry entry : appReviewerMap.entrySet()) {
            Set<String> reviewerSet = (Set) entry.getValue();
            for (String reviewerId : reviewerSet) {
                if (reviewerAppMap.get(reviewerId).size() >= 3) {
                    bw.write(reviewerId);
                    bw.write(" ");
                }
            }
            bw.newLine();
        }
        bw.flush();
        bw.close();
        System.out.println("cluster" + clusterId + " 写入ok");
    }
}
