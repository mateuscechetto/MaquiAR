package com.maquiAR

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maquiAR.arface.AugmentedFaceFragment
import com.maquiAR.arface.AugmentedFaceListener
import com.maquiAR.arface.AugmentedFaceNode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AugmentedFaceListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (face_view as AugmentedFaceFragment).setAugmentedFaceListener(this)
    }
  
    override fun onFaceAdded(face: AugmentedFaceNode) {}

    override fun onFaceUpdate(face: AugmentedFaceNode) {}
}