package ru.brauer.nearspace.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import coil.load
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.brauer.nearspace.databinding.FragmentMainBinding
import ru.brauer.nearspace.domain.repository.Repository
import ru.brauer.nearspace.domain.repository.RepositoryImpl
import ru.brauer.nearspace.domain.repository.dto.ApodDTO

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = RepositoryImpl()
        repository.getApod(object : Callback<ApodDTO> {
            override fun onResponse(call: Call<ApodDTO>, response: Response<ApodDTO>) {
                val serverResponse = response.body()
                if (response.isSuccessful && serverResponse != null) {
                    _binding?.let {
                        it.astronomyPictureOfTheDey.load(serverResponse.url)
                    }
                } else {
                    context.let {
                        Toast.makeText(it, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ApodDTO>, t: Throwable) {
                context.let {
                    Toast.makeText(it, t.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}