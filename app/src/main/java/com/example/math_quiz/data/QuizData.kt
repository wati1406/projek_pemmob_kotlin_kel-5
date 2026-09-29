package com.example.math_quiz.data

data class QuizQuestion(
    val id: Int,
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String
)

object QuizRepository {

    fun getQuestionsByLevel(level: Int): List<QuizQuestion> {
        return when (level) {
            1 -> getLevel1Questions()
            2 -> getLevel2Questions()
            3 -> getLevel3Questions()
            else -> getLevel1Questions()
        }
    }

    private fun getLevel1Questions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(1, "5 + 3 = ?", listOf("7", "8", "9", "10"), "8"),
            QuizQuestion(2, "10 + 5 = ?", listOf("10", "15", "20", "25"), "15"),
            QuizQuestion(3, "12 - 4 = ?", listOf("6", "8", "10", "16"), "8"),
            QuizQuestion(4, "7 + 6 = ?", listOf("11", "12", "13", "14"), "13"),
            QuizQuestion(5, "15 - 7 = ?", listOf("7", "8", "9", "10"), "8"),
            QuizQuestion(6, "9 + 9 = ?", listOf("16", "17", "18", "19"), "18"),
            QuizQuestion(7, "20 - 8 = ?", listOf("11", "12", "13", "14"), "12"),
            QuizQuestion(8, "14 + 6 = ?", listOf("18", "19", "20", "22"), "20"),
            QuizQuestion(9, "18 - 9 = ?", listOf("9", "10", "11", "12"), "9"),
            QuizQuestion(10, "11 + 7 = ?", listOf("16", "17", "18", "19"), "18"),
            QuizQuestion(11, "13 - 5 = ?", listOf("7", "8", "9", "10"), "8"),
            QuizQuestion(12, "16 + 4 = ?", listOf("18", "19", "20", "22"), "20"),
            QuizQuestion(13, "25 - 10 = ?", listOf("5", "10", "15", "20"), "15"),
            QuizQuestion(14, "8 + 7 = ?", listOf("13", "14", "15", "16"), "15"),
            QuizQuestion(15, "30 - 12 = ?", listOf("16", "18", "20", "22"), "18")
        )
    }

    private fun getLevel2Questions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(1, "4 × 3 = ?", listOf("10", "12", "14", "16"), "12"),
            QuizQuestion(2, "15 ÷ 3 = ?", listOf("3", "4", "5", "6"), "5"),
            QuizQuestion(3, "6 × 5 = ?", listOf("25", "30", "35", "40"), "30"),
            QuizQuestion(4, "24 ÷ 4 = ?", listOf("4", "5", "6", "8"), "6"),
            QuizQuestion(5, "7 × 4 = ?", listOf("24", "26", "28", "30"), "28"),
            QuizQuestion(6, "36 ÷ 6 = ?", listOf("4", "5", "6", "7"), "6"),
            QuizQuestion(7, "8 × 3 = ?", listOf("21", "24", "27", "30"), "24"),
            QuizQuestion(8, "40 ÷ 5 = ?", listOf("6", "7", "8", "9"), "8"),
            QuizQuestion(9, "9 × 4 = ?", listOf("32", "34", "36", "38"), "36"),
            QuizQuestion(10, "48 ÷ 8 = ?", listOf("5", "6", "7", "8"), "6"),
            QuizQuestion(11, "7 × 7 = ?", listOf("42", "47", "49", "56"), "49"),
            QuizQuestion(12, "63 ÷ 9 = ?", listOf("7", "8", "9", "10"), "7"),
            QuizQuestion(13, "8 × 6 = ?", listOf("42", "46", "48", "54"), "48"),
            QuizQuestion(14, "56 ÷ 7 = ?", listOf("6", "7", "8", "9"), "8"),
            QuizQuestion(15, "9 × 6 = ?", listOf("45", "54", "63", "72"), "54")
        )
    }

    private fun getLevel3Questions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(1, "12 × 4 = ?", listOf("44", "46", "48", "52"), "48"),
            QuizQuestion(2, "75 ÷ 5 = ?", listOf("12", "15", "18", "20"), "15"),
            QuizQuestion(3, "15 × 6 = ?", listOf("75", "80", "90", "100"), "90"),
            QuizQuestion(4, "96 ÷ 8 = ?", listOf("11", "12", "13", "14"), "12"),
            QuizQuestion(5, "14 × 5 = ?", listOf("60", "65", "70", "75"), "70"),
            QuizQuestion(6, "108 ÷ 9 = ?", listOf("11", "12", "13", "14"), "12"),
            QuizQuestion(7, "18 × 3 = ?", listOf("48", "52", "54", "56"), "54"),
            QuizQuestion(8, "120 ÷ 6 = ?", listOf("15", "20", "25", "30"), "20"),
            QuizQuestion(9, "25 × 4 = ?", listOf("75", "100", "125", "150"), "100"),
            QuizQuestion(10, "144 ÷ 12 = ?", listOf("10", "11", "12", "14"), "12"),
            QuizQuestion(11, "16 × 5 = ?", listOf("70", "75", "80", "85"), "80"),
            QuizQuestion(12, "135 ÷ 9 = ?", listOf("13", "15", "17", "19"), "15"),
            QuizQuestion(13, "22 × 3 = ?", listOf("62", "64", "66", "68"), "66"),
            QuizQuestion(14, "150 ÷ 25 = ?", listOf("4", "5", "6", "7"), "6"),
            QuizQuestion(15, "30 × 4 = ?", listOf("100", "110", "120", "130"), "120")
        )
    }
}
