package com.mog.bondoman.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mog.bondoman.R
import com.mog.bondoman.TransactionReceiver
import com.mog.bondoman.data.connection.SessionManager
import com.mog.bondoman.databinding.FragmentSettingsBinding
import com.mog.bondoman.ui.transaction.TransactionViewModel
import com.mog.bondoman.utils.Randomize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.OutputStream
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val transactionViewModel: TransactionViewModel by activityViewModels<TransactionViewModel>()
    private val saveFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("Save Transaction URI", result.data?.data.toString())
                val uri = result.data?.data

                if (uri != null) {
                    var isSuccess: Boolean
                    CoroutineScope(Dispatchers.IO).launch {
                        val ostream = requireActivity().contentResolver.openOutputStream(uri)


                        if (ostream != null) {
                            isSuccess = saveTransactions(ostream).await()
                            ostream.close()

                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    if (isSuccess) getString(R.string.save_transactions_success) else getString(
                                        R.string.save_transactions_fail
                                    ),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var isXls = false

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.logout.setOnClickListener {
            onLogoutClick()
        }
        binding.saveTransactions.setOnClickListener {
            onSaveTransactionsClick()
        }
        binding.sendTransaction.setOnClickListener {
            broadcastRandomTransaction()
        }

        binding.xls.setOnCheckedChangeListener { _, isChecked ->
            Log.d(
                "Settings File Format",
                if (isChecked) getString(R.string.xls) else getString(R.string.xlsx)
            )
            isXls = isChecked
        }
        isXls = binding.xls.isChecked

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onSaveTransactionsClick() {
        val fileExplorerIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type =
                if (isXls) "application/vnd.ms-excel" else "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            putExtra(
                Intent.EXTRA_MIME_TYPES, arrayOf(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xlsx
                    "application/vnd.ms-excel" // .xls
                )
            )
        }

        saveFileLauncher.launch(fileExplorerIntent)
    }

    private fun onLogoutClick() {
        sessionManager.removeAuthToken()
    }

    private fun broadcastRandomTransaction() {
        val intent = Intent("com.mog.bondoman.ADD_TRANSACTION")

        val args = Bundle().apply {
            this.putString(TransactionReceiver.EXTRA_TRANSACTION_TITLE, Randomize.string())
            this.putString(TransactionReceiver.EXTRA_TRANSACTION_CATEGORY, Randomize.string())
        }

        intent.putExtras(args)
        requireActivity().sendBroadcast(intent)
    }

    private suspend fun saveTransactions(output: OutputStream): Deferred<Boolean> {
        return CoroutineScope(Dispatchers.IO).async {
            try {
                val transactionData = transactionViewModel.transactions
                val dataSnapshot = transactionData.value

                // create excel workbook
                // https://www.baeldung.com/kotlin/excel-read-write
                val workbook = if (isXls) HSSFWorkbook() else XSSFWorkbook()
                val worksheet = workbook.createSheet()

                withContext(Dispatchers.Default) {
                    // setup header for table
                    // Tanggal, Kategori Transaksi (Pemasukan atau Pengeluaran), Nominal Transaksi, Nama Transaksi, dan Lokasi
                    val columnAttributes = arrayOf(
                        getString(R.string.column_date),
                        getString(R.string.column_transaction_category),
                        getString(R.string.column_nominal),
                        getString(R.string.column_transaction_name),
                        getString(R.string.column_location)
                    )

                    val headerRow = worksheet.createRow(0)
                    columnAttributes.forEachIndexed { index, value ->
                        val headerCell = headerRow.createCell(index)
                        headerCell.setCellValue(value)
                    }


                    // fill worksheet with values of transactions
                    dataSnapshot?.forEachIndexed { index, transaction ->
                        val transactionRow = worksheet.createRow(index + 1)

                        transactionRow.createCell(0).setCellValue(transaction.date.toString())
                        transactionRow.createCell(1).setCellValue(transaction.category)
                        transactionRow.createCell(2).setCellValue(transaction.nominal)
                        transactionRow.createCell(3).setCellValue(transaction.title)
                        transactionRow.createCell(4).setCellValue(transaction.location)
                    }
                }

                // write excel to output stream
                withContext(Dispatchers.IO) {
                    workbook.write(output)
                    workbook.close()
                }

                return@async true
            } catch (e: Exception) {
                e.printStackTrace()

                return@async false
            }
        }

    }
}