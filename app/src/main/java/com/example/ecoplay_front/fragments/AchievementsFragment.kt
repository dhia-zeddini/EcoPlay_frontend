    package com.example.ecoplay_front.fragments

    import android.content.Intent
    import android.os.Bundle
    import android.util.Log
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.LinearLayout
    import android.widget.ProgressBar
    import android.widget.TextView
    import androidx.appcompat.app.AppCompatActivity
    import androidx.fragment.app.viewModels
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.example.ecoplay_front.R
    import com.example.ecoplay_front.adapter.CarouselAdapter
    import com.example.ecoplay_front.adapter.ProfileAvatarAdapter
    import com.example.ecoplay_front.model.ProductModel
    import com.example.ecoplay_front.uttil.SpaceItemDecoration
    import com.example.ecoplay_front.view.PREF_FILE
    import com.example.ecoplay_front.view.TOKEN
    import com.example.ecoplay_front.viewModel.CarouselViewModel
    import com.example.ecoplay_front.viewModel.ProfileViewModel


    class AchievementsFragment : Fragment() {


        private val viewModel by viewModels<ProfileViewModel>()
        private val productViewModel by viewModels<CarouselViewModel>()
        private lateinit var recyclerView: RecyclerView
        private lateinit var profileAvatarAdapter: ProfileAvatarAdapter
        private var owned: MutableList<String> = mutableListOf()
        private var avatars: MutableList<ProductModel> = mutableListOf()
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            val view = inflater.inflate(R.layout.fragment_achivements, container, false)

            var progressBar: ProgressBar =view.findViewById(R.id.progressBar)
            var levelMainIcon: TextView =view.findViewById(R.id.levelMainIcon)
            var levelHeader: TextView =view.findViewById(R.id.levelHeader)
            var levelSecondIcon: TextView =view.findViewById(R.id.levelSecondIcon)
            var tvPoints: TextView =view.findViewById(R.id.tvPoints)
            var tvNbrGold: TextView =view.findViewById(R.id.tvNbrGold)
            var tvNbrSilver: TextView =view.findViewById(R.id.tvNbrSilver)
            var tvNbrBronze: TextView =view.findViewById(R.id.tvNbrBronze)
            var pointsIndicator: TextView =view.findViewById(R.id.pointsIndicator)
            var nextLevelSecondIcon: TextView =view.findViewById(R.id.nextLevelSecondIcon)
            var layoutGold: LinearLayout =view.findViewById(R.id.layoutGold)

            layoutGold.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

            val mSharedPreferences = requireContext().getSharedPreferences(PREF_FILE, AppCompatActivity.MODE_PRIVATE)
            var token:String?=mSharedPreferences.getString(TOKEN,"no token")
            Log.d("RetrofitCall","prefs${token}")


            // Fetch profile and products data
            viewModel.getProfile(token ?: "")
            productViewModel.fetchProductsFromBackend()

            setupObservers()



            /*viewModel.userProfile.observe(viewLifecycleOwner) { userModel ->
                levelMainIcon.text = userModel.level.toString()
                levelSecondIcon.text = userModel.level.toString()
                nextLevelSecondIcon.text = (userModel.level+1).toString()
                tvPoints.text = userModel.score.toString()
                levelHeader.text = "Level ${userModel.level}"
                pointsIndicator.text = "${userModel.score} Points to next level"
                progressBar.progress = userModel.score
                tvNbrGold.text= userModel.goldMedal.toString()
                tvNbrSilver.text=userModel.silverMedal.toString()
                tvNbrBronze.text=userModel.bronzeMedal.toString()

                owned = userModel.owned.toMutableList() */




                /* for (prodId in owned){
                     Log.d("avatar","owned: ${prodId}")

                     productViewModel.fetchProductById(prodId)
                     productViewModel.prod.observe(viewLifecycleOwner){product->

                         Log.d("avatar","avatars: ${product.image}")
                         avatars.add(product)
                     }
                 }*/

                //Log.d("avatar","avatarsssss: ${avatars}")

                /////////
                /*recyclerView = view.findViewById(R.id.avatarRecyclerView)
                recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                val spacingInPixels = resources.getDimensionPixelSize(R.dimen.carousel_item_spacing)
                recyclerView.addItemDecoration(SpaceItemDecoration(spacingInPixels))
                profileAvatarAdapter = ProfileAvatarAdapter(mutableListOf())
                recyclerView.adapter = profileAvatarAdapter

                profileAvatarAdapter.updateData(avatars)*/

                /*productViewModel.fetchProductsFromBackend()
                productViewModel.products.observe(viewLifecycleOwner){products->


                    avatars=products

                    Log.d("test avatars", "Owned data: ${avatars}")

                }  */








            return view
        }

        private fun setupObservers() {
            // Observe userProfile changes
            viewModel.userProfile.observe(viewLifecycleOwner) { userModel ->

                owned = userModel.owned.toMutableList()
                Log.d("test owned", "Owned data: ${owned}")

                // Assuming you need to use 'owned' to filter 'avatars'
                updateAvatarsList()
            }

            // Observe products changes
            productViewModel.products.observe(viewLifecycleOwner) { products ->
                avatars.clear()
                avatars.addAll(products)
                Log.d("test avatars", "Avatars data: ${avatars}")

                updateAvatarsList()
            }
        }

        private fun updateAvatarsList() {
            val filteredAvatars = avatars.filter { productModel ->
                productModel._id in owned
            }

            // Update RecyclerView Adapter
        }





    }