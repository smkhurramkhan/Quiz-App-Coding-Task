package com.example.codingchallange.ui.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.codingchallange.R
import com.example.codingchallange.databinding.ActivityMainBinding
import com.example.codingchallange.databinding.ItemOptionBinding
import com.example.codingchallange.roomdb.entity.HighScoreEntity
import com.example.codingchallange.roomdb.entity.Question
import com.example.codingchallange.ui.main.viewmodel.QuestionViewModel
import com.example.codingchallange.ui.main.viewmodel.QuizViewModel
import com.example.codingchallange.utils.CustomDialog
import com.example.codingchallange.utils.ExtensionFunctions.hide
import com.example.codingchallange.utils.ExtensionFunctions.show
import com.example.codingchallange.utils.SharedPrefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Random

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private val viewModel: QuizViewModel by viewModels()
    private val questionModel: QuestionViewModel by viewModels()
    private var optionSelected = mutableListOf<String>()

    private var quizList = mutableListOf<Question>()

    private var questionTotal: Int = 0

    private var score = 0

    private var selectedOptions = mutableListOf<Int>()

    private var sharedPrefs: SharedPrefs? = null

    private var loadingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        sharedPrefs = SharedPrefs(this)

        questionModel.questionNumber.postValue(0)

        initToolbar()

        setupObservers()

    }


    private fun initToolbar() {
        val title = getString(R.string.app_name)
        mainBinding.toolbar.title = title
        setSupportActionBar(mainBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return false
    }

    private fun setupObservers() {

        viewModel.localQuiz.observe(this) {

            if (!it.isNullOrEmpty()) {
                Timber.d("questions length rec is ${it[0].questions.size}")


                quizList.clear()


                val list = it[0].questions.toMutableList()

                list.shuffle()

                list.let { it1 ->
                    quizList.addAll(it1)
                }

                Timber.d(quizList.toString())


                questionTotal = quizList.size

                mainBinding.lpiProgress.progress = 1
                mainBinding.lpiProgress.max = questionTotal

                mainBinding.tvQuestionNumber.text =
                    buildString {
                        append("Questions ")
                        append(mainBinding.lpiProgress.progress)
                        append(" of ")
                        append(questionTotal)
                    }

                Timber.d("random question is ${quizList[Random().nextInt(quizList.size)]}")


                questionModel.questionNumber.observe(this) { qNum ->


                    val question = quizList[qNum]
                    mainBinding.lpiProgress.progress = qNum + 1
                    updateQuestion(question)


                    mainBinding.tvQuestionNumber.text = getString(
                        R.string.question_progress,
                        if ((qNum + 1).toString().length > 1) (qNum + 1).toString() else "0${(qNum + 1)}",
                        if (questionTotal.toString().length > 1) questionTotal.toString() else "0$questionTotal"
                    )

                    mainBinding.btnNext.text =
                        if (qNum == questionTotal - 1) "Submit" else getString(R.string.next)


                    mainBinding.btnNext.setOnClickListener {
                        if (qNum == questionTotal - 1) {
                            // timer.cancel()
                            evaluateQuizResult(question)
                            showResult(totalQuestions = questionTotal)
                        } else {

                            if (selectedOptions.size > 0) {
                                evaluateQuizResult(question)
                                // delay

                                lifecycleScope.launch {
                                    loadingDialog = CustomDialog.setLoadingDialog(
                                        this@MainActivity,
                                        false
                                    )

                                    delay(2000)
                                    optionSelected.clear()
                                    resetOptions()
                                    questionModel.questionNumber.postValue(qNum + 1)

                                    loadingDialog?.dismiss()

                                }

                            }

                        }
                    }
                }
            }
        }
    }


    private fun resetOptions() {
        selectedOptions.clear()
        mainBinding.option1.llOption.background =
            ContextCompat.getDrawable(this, R.drawable.button_outline)
        mainBinding.option2.llOption.background =
            ContextCompat.getDrawable(this, R.drawable.button_outline)
        mainBinding.option3.llOption.background =
            ContextCompat.getDrawable(this, R.drawable.button_outline)
        mainBinding.option4.llOption.background =
            ContextCompat.getDrawable(this, R.drawable.button_outline)
        mainBinding.option5.llOption.background =
            ContextCompat.getDrawable(this, R.drawable.button_outline)
    }

    @SuppressLint("StringFormatInvalid")
    private fun showResult(totalQuestions: Int) {

        val highScoreEntity = HighScoreEntity(
            username = sharedPrefs?.getUserName()!!,
            score = score,
            time = System.currentTimeMillis()
        )

        insertScore(highScoreEntity)

        mainBinding.clContent.visibility = View.GONE
        mainBinding.clControls.visibility = View.GONE
        mainBinding.quizResult.apply {
            clResults.visibility = View.VISIBLE
            if (score == totalQuestions) {
                tvResult.text = getString(R.string.congratulations)
            } else {
                tvResult.text = getString(R.string.better_luck_next_time)
            }

            tvScore.text = "$score"

            btnShareResults.setOnClickListener {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.share_results, score, totalQuestions)
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }

        }
    }

    private fun evaluateQuizResult(question: Question) {
        val answersList = mutableListOf<String>()

        if (question.type == "single-choice") {
            answersList.add(question.correctAnswer)
        } else {
            answersList.addAll(question.correctAnswer.split(","))
        }


        val validatedAnswers = mutableListOf<String>()
        val wrongInoutedAnswers = mutableListOf<String>()


        optionSelected.forEach { selected ->

            if (answersList.contains(selected)) {
                validatedAnswers.add(selected)
            } else {
                wrongInoutedAnswers.add(selected)
            }
        }

        Timber.d(validatedAnswers.toString())


        answersList.forEach { answer ->
            when (answer) {
                "A" -> {
                    markCorrect(mainBinding.option1)
                }

                "B" -> {
                    markCorrect(mainBinding.option2)
                }

                "C" -> {
                    markCorrect(mainBinding.option3)
                }

                "D" -> {
                    markCorrect(mainBinding.option4)
                }

                "E" -> {
                    markCorrect(mainBinding.option5)
                }

                else -> {
                    // Code block for other cases (if needed)
                }
            }
        }

        wrongInoutedAnswers.forEach { answer ->
            when (answer) {
                "A" -> {
                    markWrong(mainBinding.option1)
                }

                "B" -> {
                    markWrong(mainBinding.option2)
                }

                "C" -> {
                    markWrong(mainBinding.option3)
                }

                "D" -> {
                    markWrong(mainBinding.option4)
                }

                "E" -> {
                    markWrong(mainBinding.option5)
                }

                else -> {
                    // Code block for other cases (if needed)
                }
            }
        }


        if (validatedAnswers.isNotEmpty()) {
            Timber.d("condition is True and correct ans is ${question.correctAnswer}")
            score += question.score
        }
    }


    private fun selectOption(view: ItemOptionBinding, option: Int) {
        if (selectedOptions.contains(option)) {
            selectedOptions.remove(option)
            view.llOption.background =
                ContextCompat.getDrawable(this, R.drawable.button_outline)
        } else {
            selectedOptions.add(option)
            view.llOption.background =
                ContextCompat.getDrawable(this, R.drawable.correct_option_border_bg)
        }
    }

    private fun markCorrect(view: ItemOptionBinding) {
        view.llOption.background =
            ContextCompat.getDrawable(this, R.drawable.correct_option_border_bg)
    }

    private fun markWrong(view: ItemOptionBinding) {
        view.llOption.background =
            ContextCompat.getDrawable(this, R.drawable.wrong_option_border_bg)
    }

    private fun updateQuestion(question: Question) {
        mainBinding.tvQuestion.text = question.question
        Glide.with(this)
            .load(question.questionImageUrl)
            .into(mainBinding.questionImage)

        showIfQuestionCOntentExists(
            mainBinding.option1.tvOption,
            question.answers.A,
            mainBinding.option1.llOption
        )
        showIfQuestionCOntentExists(
            mainBinding.option2.tvOption,
            question.answers.B,
            mainBinding.option2.llOption
        )
        showIfQuestionCOntentExists(
            mainBinding.option3.tvOption,
            question.answers.C,
            mainBinding.option3.llOption
        )
        showIfQuestionCOntentExists(
            mainBinding.option4.tvOption,
            question.answers.D,
            mainBinding.option4.llOption
        )
        showIfQuestionCOntentExists(
            mainBinding.option5.tvOption,
            question.answers.E,
            mainBinding.option5.llOption
        )





        mainBinding.option1.llOption.setOnClickListener {
            optionSelected.add("A")
            selectOption(mainBinding.option1, 1)
        }
        mainBinding.option2.llOption.setOnClickListener {
            optionSelected.add("B")
            selectOption(mainBinding.option2, 2)
        }
        mainBinding.option3.llOption.setOnClickListener {
            optionSelected.add("C")
            selectOption(mainBinding.option3, 3)
        }
        mainBinding.option4.llOption.setOnClickListener {
            optionSelected.add("D")
            selectOption(mainBinding.option4, 4)
        }
        mainBinding.option5.llOption.setOnClickListener {
            optionSelected.add("E")
            selectOption(mainBinding.option5, 5)
        }
    }

    private fun showIfQuestionCOntentExists(
        tvOption: TextView,
        a: String?,
        option1: View
    ) {
        if (a.isNullOrEmpty()) {
            option1.hide()
        } else {
            option1.show()
            tvOption.text = a
        }
    }


    private fun insertScore(highScoreEntity: HighScoreEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.insertScore(highScoreEntity)
        }
    }

}