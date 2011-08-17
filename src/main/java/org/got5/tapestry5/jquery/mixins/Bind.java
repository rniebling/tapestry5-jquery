package org.got5.tapestry5.jquery.mixins;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.got5.tapestry5.jquery.EffectsConstants;

@Import(library = { "${assets.path}/mixins/bind/bind.js" })
public class Bind {

	@Parameter(defaultPrefix = "literal")
	private String element;

	@Parameter(defaultPrefix = "literal")
	private String event;

	@Parameter(defaultPrefix = "literal")
	private String hide;

	@Parameter(defaultPrefix = "literal", value = "slide")
	private String hideEffect;

	@Parameter(defaultPrefix = "literal", value = "500")
	private String hideTime;

	@Parameter
	private Map<String, String> hideOptions;

	@Parameter(defaultPrefix = "literal")
	private String zone;

	@Parameter(defaultPrefix = "literal", value = "highlight")
	private String zoneUpdate;

	@Parameter(defaultPrefix = "literal")
	private String callback;

	@Parameter
	private Object[] context;

	@Parameter(defaultPrefix = "literal")
	private String title;
	
	@Parameter(defaultPrefix = "literal")
	private String history;

	@Parameter(defaultPrefix = "literal")
	private String eventType;

	@Parameter(value = "true")
	private Boolean preventDefault;

	@Parameter(value = "true")
	private Boolean doImports;

	@Parameter(value = "CoNtExT", defaultPrefix = "literal")
	private String contextMarker;

	@Inject
	private AssetSource assetSource;

	@Inject
	private JavaScriptSupport javaScriptSupport;

	@InjectContainer
	private ClientElement clientElement;

	@Inject
	private ComponentResources resources;

	String element() {
		if (element != null) {
			return element;
		}
		return clientElement.getClientId();
	}

	String eventType() {
		if (eventType != null) {
			return eventType;
		}
		return this.getClass().getSimpleName().toLowerCase();
	}

	String event() {
		return event;
	}

	Link createLink(String event, String marker) {
		ComponentResources parent = resources.getContainerResources()
				.getContainerResources();
		Object[] o = context;
		if (callback != null) {
			if (o == null) {
				o = new Object[1];
			} else {
				o = new Object[o.length + 1];
				System.arraycopy(context, 0, o, 0, context.length);
			}
			o[o.length - 1] = marker;
		}
		return parent.createEventLink(event, o);
	}

	public void afterRender() {
		JSONObject spec = new JSONObject();

		spec.put("elementId", element());
		spec.put("eventType", eventType());
		if (event() != null) {
			spec.put("url", createLink(event(), contextMarker).toAbsoluteURI());
			spec.put("contextMarker", contextMarker);
		}
		spec.put("preventDefault", preventDefault);
		spec.put("hide", hide);
		spec.put("hideEffect", hideEffect);
		spec.put("hideTime", hideTime);
		if (hideOptions != null) {
			JSONObject joptions = new JSONObject();

			for (Entry<String, String> o : hideOptions.entrySet()) {
				joptions.put(o.getKey(), o.getValue());
			}
			spec.put("hideOptions", joptions);
		}
		spec.put("zoneId", zone);
		spec.put("zoneUpdate", zoneUpdate);
		spec.put("title", title);
		spec.put("history", history);
		// Does not work with AJAX JSON return
		// if ( jcontext != null ) {
		// spec.put("jcontext", new JSONLiteral(jcontext));
		// }
		spec.put("callback", callback);
		javaScriptSupport.addInitializerCall("jqbind", spec);
		if (doImports) {
			if (zoneUpdate != null) {
				String effect = findEffect(zoneUpdate);
				if ( effect != null ) {
					javaScriptSupport.importJavaScriptLibrary(assetSource.getExpandedAsset(effect));
				}
			}
			if (hideEffect != null) {
				String effect = findEffect(zoneUpdate);
				if ( effect != null ) {
					javaScriptSupport.importJavaScriptLibrary(assetSource.getExpandedAsset(effect));
				}
			}
		}
	}
	
	private String findEffect(String effect) {
		effect = effect.toUpperCase();
		if ( effect.equals("BLIND")) {
			return EffectsConstants.BLIND;
		}
		if ( effect.equals("BOUNCE")) {
			return EffectsConstants.BOUNCE;
		}
		if ( effect.equals("CLIP")) {
			return EffectsConstants.CLIP;
		}
		if ( effect.equals("DROP")) {
			return EffectsConstants.DROP;
		}
		if ( effect.equals("EXPLODE")) {
			return EffectsConstants.EXPLODE;
		}
		if ( effect.equals("FOLD")) {
			return EffectsConstants.FOLD;
		}
		if ( effect.equals("HIGHLIGHT")) {
			return EffectsConstants.HIGHLIGHT;
		}
		if ( effect.equals("PULSATE")) {
			return EffectsConstants.PULSATE;
		}
		if ( effect.equals("SCALE")) {
			return EffectsConstants.SCALE;
		}
		if ( effect.equals("SHAKE")) {
			return EffectsConstants.SHAKE;
		}
		if ( effect.equals("SLIDE")) {
			return EffectsConstants.SLIDE;
		}
		if ( effect.equals("TRANSFER")) {
			return EffectsConstants.TRANSFER;
		}
		return null;
	}
	
}