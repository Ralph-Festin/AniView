package com.example.aniview.ui.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilterDropdown(
    label: String,
    options: List<String>,
    selectedOptions: Set<String>,
    onOptionToggle: (String) -> Unit,
    singleSelection: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 14.sp
            )
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp
                else Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            val maxVisibleRows = 30
            val maxHeight = 400.dp

            Column(
                modifier = Modifier
                    .heightIn(max = maxHeight)
                    .verticalScroll(scrollState)
                    .padding(8.dp)
            ) {
                val visibleRows = options.chunked(2).take(maxVisibleRows)

                visibleRows.forEach { rowOptions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowOptions.forEach { option ->
                            val isSelected = option in selectedOptions

                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        if (singleSelection) {
                                            if (isSelected) {
                                                onOptionToggle("")
                                            } else {
                                                onOptionToggle(option)
                                            }
                                        } else {
                                            onOptionToggle(option)
                                        }
                                    }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (singleSelection) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = {
                                            if (isSelected) {
                                                onOptionToggle("")
                                            } else {
                                                onOptionToggle(option)
                                            }
                                        }
                                    )
                                } else {
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = { onOptionToggle(option) }
                                    )
                                }

                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = option,
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.widthIn(max = 80.dp)
                                )
                            }
                        }

                        repeat(2 - rowOptions.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}


