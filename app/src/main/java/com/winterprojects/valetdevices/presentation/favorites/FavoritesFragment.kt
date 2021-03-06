package com.winterprojects.valetdevices.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.winterprojects.valetdevices.R
import com.winterprojects.valetdevices.common.helpers.gone
import com.winterprojects.valetdevices.common.helpers.visible
import com.winterprojects.valetdevices.databinding.FragmentFavoritesBinding
import com.winterprojects.valetdevices.domain.devices.models.DeviceFavoriteModel
import com.winterprojects.valetdevices.helpers.StateResult
import org.koin.android.ext.android.inject

class FavoritesFragment : Fragment(),
    OnRemoveFavoriteItemClickListener {
    private var snackBar: Snackbar? = null
    private val favoritesViewModel: FavoritesViewModel by inject()

    private lateinit var favoriteAdapter: FavoriteAdapter

    private lateinit var fragmentFavoritesBinding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentFavoritesBinding = FragmentFavoritesBinding.inflate(
            inflater,
            container,
            false
        )

        initializeAdapters()
        initializeObservers()

        return fragmentFavoritesBinding.root
    }

    private fun initializeAdapters() {
        favoriteAdapter = FavoriteAdapter( this)
        fragmentFavoritesBinding.recyclerViewFavoriteDevice.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initializeObservers() {
        favoritesViewModel.favorites.observe(viewLifecycleOwner) { result ->
            when (result) {
                is StateResult.Loaded -> {
                    favoriteAdapter.submitList(result.data)
                    showDataState()
                }
                is StateResult.Empty -> {
                    showEmptyState()
                }
                is StateResult.ErrorState -> {
                    Toast.makeText(context, result.errorMsg, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun showEmptyState() {
        fragmentFavoritesBinding.emptyStateLayout.emptyState.visible()
        fragmentFavoritesBinding.recyclerViewFavoriteDevice.gone()
    }

    private fun showDataState() {
        fragmentFavoritesBinding.emptyStateLayout.emptyState.gone()
        fragmentFavoritesBinding.recyclerViewFavoriteDevice.visible()
    }

    override fun onRemoveFavoriteItemClickListener(
        itemPosition: Int,
        favoriteDeviceModel: DeviceFavoriteModel
    ) {
        favoriteAdapter.notifyItemRemoved(itemPosition)
        favoritesViewModel.removeFavoriteItem(itemPosition, favoriteDeviceModel)
        showUndoRemoveFavorite()
    }

    private fun showUndoRemoveFavorite() {
        snackBar?.dismiss()
        snackBar = Snackbar.make(
            requireView(), R.string.undo_remove_favorite,
            Snackbar.LENGTH_LONG
        )
        snackBar?.setAction(R.string.undo) { _ -> undoDelete() }
        snackBar?.show()
    }

    private fun undoDelete() {
        favoriteAdapter.notifyItemInserted(favoritesViewModel.lastPositionDeviceFavoriteRemoved)
        favoritesViewModel.undoRemoveFavoriteItem()
    }
}