package jose.suarez.com.josesuarezeva2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import jose.suarez.com.josesuarezeva2.Api.RetrofitClient
import jose.suarez.com.josesuarezeva2.Database.TechDatabase
import jose.suarez.com.josesuarezeva2.Models.MainViewModel
import jose.suarez.com.josesuarezeva2.Models.MainViewModelFactory
import jose.suarez.com.josesuarezeva2.Repository.AuditRepository

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}