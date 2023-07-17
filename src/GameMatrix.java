public class GameMatrix {
    private int[][] matrix;
    public GameMatrix(int n, int m, int level){
        matrix = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = level;
            }
        }
    }
    public int[][] getMatrix(){
        return matrix;
    }
//    public void hit(int i, int j){
//        if (matrix[i][j] > 0)
//            matrix[i][j]--;
//    }
//    public void newLevel(int n, int m, int level){
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < m; j++) {
//                matrix[i][j] = i + level - 1;
//            }
//        }
//    }
}
