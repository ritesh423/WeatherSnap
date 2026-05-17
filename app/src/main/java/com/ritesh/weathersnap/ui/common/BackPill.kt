package com.ritesh.weathersnap.ui.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ritesh.weathersnap.ui.theme.ForestGreen
import com.ritesh.weathersnap.ui.theme.MintSoft

@Composable
fun BackPill(label: String = "Back", onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = ForestGreen,
            contentColor = MintSoft
        )
    ) {
        Text(label)
    }
}
