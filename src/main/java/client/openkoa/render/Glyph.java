package client.openkoa.render;

import client.openkoa.render.GlyphPage;

record Glyph(int u, int v, int width, int height, char value, GlyphPage owner) {
}