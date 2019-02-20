
#pragma version(1)
#pragma rs java_package_name(com.subgarden.airbrush)
#pragma rs_fp_relaxed

// set from the java SDK level
rs_allocation gIn;
rs_allocation gOut;
rs_script gScript;
int32_t gTopLeftColor;
int32_t gTopRightColor;
int32_t gBottomRightColor;
int32_t gBottomLeftColor;
int32_t gWidthPixels;
int32_t gHeightPixels;

static float2 topLeftCorner;
static float2 topRightCorner;
static float2 bottomRightCorner;
static float2 bottomLeftCorner;

static float2 vector(uint32_t x, uint32_t y);

void setup() {
    topLeftCorner = vector(0, 0);
    topRightCorner = vector(gWidthPixels, 0);
    bottomRightCorner = vector(gWidthPixels, gHeightPixels);
    bottomLeftCorner = vector(0, gHeightPixels);
}

void filter() {
    setup();
    rsForEach(gScript, gIn, gOut);	// for each element of the input allocation,
    										// call root() method on gScript
}

static float getDistance(float2 currentVector, float2 cornerVector, float2 oppositeCornerVector) {

    return 1 - distance(cornerVector, currentVector) / distance(cornerVector, oppositeCornerVector);
}

static float3 getAdjustedColor(int32_t color, float dist) {
    uint32_t red = (color >> 16) & 0xFF;
    uint32_t green = (color >> 8) & 0xFF;
    uint32_t blue = color & 0xFF;

    float3 adjustedColor;
    float adjustedRed = red * dist * dist / 255;
    float adjustedGreen = green * dist * dist / 255;
    float adjustedBlue = blue *  dist * dist / 255;

    adjustedColor.x = clamp(adjustedRed, 0.0, 1.0);
    adjustedColor.y = clamp(adjustedGreen, 0.0, 1.0);
    adjustedColor.z = clamp(adjustedBlue, 0.0, 1.0);
    return adjustedColor;
}

void root(const uchar4 *v_in, uchar4 *v_out, const void *usrData, uint32_t x, uint32_t y) {

    float2 currentVector = vector(x, y);

    float topLeftDistance = getDistance(currentVector, topLeftCorner, bottomRightCorner);
    float topRightDistance = getDistance(currentVector, topRightCorner, bottomLeftCorner);
    float bottomRightDistance = getDistance(currentVector, bottomRightCorner, topLeftCorner);
    float bottomLeftDistance = getDistance(currentVector, bottomLeftCorner, topRightCorner);

    float3 adjustedTopLeftColor = getAdjustedColor(gTopLeftColor, topLeftDistance);
    float3 adjustedTopRightColor = getAdjustedColor(gTopRightColor, topRightDistance);
    float3 adjustedBottomRightColor = getAdjustedColor(gBottomRightColor, bottomRightDistance);
    float3 adjustedBottomLeftColor = getAdjustedColor(gBottomLeftColor, bottomLeftDistance);

    float3 adjustedColor;

    adjustedColor.x = adjustedTopLeftColor.x + adjustedTopRightColor.x + adjustedBottomRightColor.x + adjustedBottomLeftColor.x;
    adjustedColor.y = adjustedTopLeftColor.y + adjustedTopRightColor.y + adjustedBottomRightColor.y + adjustedBottomLeftColor.y;
    adjustedColor.z = adjustedTopLeftColor.z + adjustedTopRightColor.z + adjustedBottomRightColor.z + adjustedBottomLeftColor.z;

    *v_out = rsPackColorTo8888(adjustedColor);
}

static float2 vector(uint32_t x, uint32_t y) {
    float2 vector;
    vector.x = x;
    vector.y = y;
    return vector;
}
