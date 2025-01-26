package com.felipemazetti.investeasy

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.felipemazetti.investeasy.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var valorInicial = 0.0

        fun formatCurrency(value: Double):String{
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            return numberFormat.format(value)
        }


        binding.btCalc.setOnClickListener {

            val valorInvestido = binding.tilAmount.text.toString()
            val qtdMeses = binding.tilMonth.text.toString()
            val taxaJuros = binding.tilInterest.text.toString()


            if (valorInvestido.isEmpty() == true || qtdMeses.isEmpty() == true || taxaJuros.isEmpty() == true) {

                Snackbar.make(binding.tilAmount, "Preencha todos os campos", Snackbar.LENGTH_LONG)
                    .show()
            } else {

                val aporteMensal = valorInvestido.toDouble()
                val qntMesesInt = qtdMeses.toInt()
                val taxaJurosDouble = taxaJuros.toDouble()

                val montante = calcularJurosCompostos(valorInicial, taxaJurosDouble, qntMesesInt, aporteMensal)


                val rendimentos = montante - (aporteMensal * qntMesesInt)

               val resultadoFormatado = formatCurrency(montante)
               val rendimentosFormatado = formatCurrency(rendimentos)
               binding.tvAmount.text = " $resultadoFormatado"
               binding.tvIncome.text = " $rendimentosFormatado"

            }

        }

        binding.btClean.setOnClickListener {
            binding.tilAmount.setText("")
            binding.tilMonth.setText("")
            binding.tilInterest.setText("")
            binding.tvAmount.setText("0.0")
            binding.tvIncome.setText("0.0")


        }


    }

    private fun calcularJurosCompostos(valorInicial: Double, taxaJurosMensal: Double,
                                       periodoMeses: Int, aporteMensal: Double): Double {

        var saldo = valorInicial
        val taxaJuros = taxaJurosMensal / 100

        for (mes in 1..periodoMeses) {
            saldo += aporteMensal
            saldo *= (1 + taxaJuros)
        }

        return saldo
    }


}