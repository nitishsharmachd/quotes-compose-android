package com.example.quotes.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotes.domain.model.Quote

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QuoteCard(
    quote: Quote,
    onFavoriteToggle: (Quote) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onDelete: ((Quote) -> Unit)? = null
) {
    val context = LocalContext.current
    val favoriteColor by animateColorAsState(
        targetValue = if (quote.isFavorite) Color(0xFFE91E63) else Color.Gray,
        label = "favoriteColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(
                onClickLabel = "View quote details",
                onClick = onClick
            ),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Chip(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.semantics {
                        contentDescription = "Category: ${quote.category.displayName}"
                    }
                ) {
                    Text(
                        text = quote.category.displayName,
                        style = MaterialTheme.typography.caption.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary
                        )
                    )
                }

                Row {
                    IconButton(onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "\"${quote.text}\" — ${quote.author}")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Quote"))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share quote by ${quote.author}",
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    IconButton(onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Quote", "\"${quote.text}\" — ${quote.author}")
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Quote copied to clipboard!", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy quote to clipboard",
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    IconButton(
                        onClick = { onFavoriteToggle(quote) },
                        modifier = Modifier.semantics {
                            stateDescription = if (quote.isFavorite) "Saved in favorites" else "Not in favorites"
                        }
                    ) {
                        Icon(
                            imageVector = if (quote.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (quote.isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = favoriteColor
                        )
                    }

                    if (quote.isCustom && onDelete != null) {
                        IconButton(onClick = { onDelete(quote) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete custom quote",
                                tint = Color.Red.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "“${quote.text}”",
                style = MaterialTheme.typography.h6.copy(
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                    lineHeight = 28.sp
                ),
                color = MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "— ${quote.author}",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colors.primary,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
