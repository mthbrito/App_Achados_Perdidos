package br.edu.ifpb.app_achados_perdidos.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import br.edu.ifpb.app_achados_perdidos.R
import br.edu.ifpb.app_achados_perdidos.ui.theme.TextoPrincipal
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import java.io.File

// Centro padrão (São José do Egito) quando o item ainda não tem coordenadas.
private const val LAT_PADRAO = -7.4733
private const val LON_PADRAO = -37.2800

/**
 * Mapa OpenStreetMap (osmdroid) com um marcador.
 * - Se [onMapaClick] for null, o mapa é só de visualização (tela de detalhes).
 * - Se for informado, o toque no mapa devolve latitude/longitude (tela do formulário).
 */
@SuppressLint("ClickableViewAccessibility")
@Composable
fun MiniMapa(
    latitude: Double?,
    longitude: Double?,
    modifier: Modifier = Modifier,
    zoom: Double = 16.0,
    onMapaClick: ((Double, Double) -> Unit)? = null
) {
    val context = LocalContext.current
    val cliqueAtual by rememberUpdatedState(onMapaClick)

    val mapView = remember {
        configurarOsmdroid(context)
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            controller.setZoom(zoom)
            controller.setCenter(GeoPoint(LAT_PADRAO, LON_PADRAO))

            // Impede que a rolagem da tela "roube" o arrastar do mapa.
            setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE ->
                        v.parent?.requestDisallowInterceptTouchEvent(true)
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                        v.parent?.requestDisallowInterceptTouchEvent(false)
                }
                false
            }

            overlays.add(MapEventsOverlay(object : MapEventsReceiver {
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    val aoClicar = cliqueAtual
                    if (p != null && aoClicar != null) {
                        aoClicar(p.latitude, p.longitude)
                        return true
                    }
                    return false
                }

                override fun longPressHelper(p: GeoPoint?): Boolean = false
            }))
        }
    }

    val marcador = remember {
        Marker(mapView).apply {
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            setOnMarkerClickListener { _, _ -> true } // sem balão de informações
        }
    }
    val jaCentralizou = remember { booleanArrayOf(false) }

    // osmdroid precisa saber quando a tela pausa/retoma para gerenciar os tiles.
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, evento ->
            when (evento) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onPause()
            mapView.onDetach()
        }
    }

    Box(modifier = modifier.clip(RoundedCornerShape(12.dp))) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                if (latitude != null && longitude != null) {
                    val ponto = GeoPoint(latitude, longitude)
                    marcador.position = ponto
                    if (!view.overlays.contains(marcador)) view.overlays.add(marcador)
                    // No formulário só centraliza na 1ª vez, para não "pular" a cada toque.
                    if (onMapaClick == null || !jaCentralizou[0]) {
                        view.controller.setCenter(ponto)
                        jaCentralizou[0] = true
                    }
                } else {
                    view.overlays.remove(marcador)
                }
                view.invalidate()
            }
        )

        // A licença do OpenStreetMap exige exibir a atribuição.
        Text(
            text = stringResource(R.string.mapa_atribuicao),
            color = TextoPrincipal,
            fontSize = 11.sp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(4.dp)
                .background(Color.White.copy(alpha = 0.75f), RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp, vertical = 1.dp)
        )
    }
}

private fun configurarOsmdroid(context: Context) {
    val config = Configuration.getInstance()
    config.load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
    config.userAgentValue = context.packageName
    config.osmdroidBasePath = File(context.cacheDir, "osmdroid")
    config.osmdroidTileCache = File(context.cacheDir, "osmdroid/tiles")
}