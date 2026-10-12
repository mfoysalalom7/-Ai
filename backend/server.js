import express from "express";
import OpenAI from "openai";
import { rateLimit } from "express-rate-limit";

const app = express();
app.disable("x-powered-by");
app.use(express.json({ limit: "20kb" }));

app.use("/chat", rateLimit({
  windowMs: 15 * 60 * 1000,
  limit: 20,
  standardHeaders: true,
  legacyHeaders: false
}));

app.get("/", (_req, res) => {
  res.json({ status: "online", service: "Cherry AI Backend" });
});

app.post("/chat", async (req, res) => {
  try {
    const { systemInstruction, userPrompt, history = [] } = req.body ?? {};

    if (
      typeof systemInstruction !== "string" ||
      typeof userPrompt !== "string" ||
      !userPrompt.trim() ||
      userPrompt.length > 8000 ||
      systemInstruction.length > 8000 ||
      !Array.isArray(history) ||
      history.length > 10
    ) {
      return res.status(400).json({ error: "Invalid request" });
    }

    const apiKey = process.env.OPENAI_API_KEY;
    if (!apiKey) {
      return res.status(500).json({ error: "AI service is not configured" });
    }

    const client = new OpenAI({ apiKey });

    const input = history
      .filter(item =>
        item &&
        ["user", "assistant"].includes(item.role) &&
        typeof item.text === "string"
      )
      .map(item => ({
        role: item.role,
        content: item.text.slice(0, 4000)
      }));

    input.push({
      role: "user",
      content: userPrompt.trim()
    });

    const response = await client.responses.create({
      model: process.env.OPENAI_MODEL || "gpt-5.5",
      instructions: systemInstruction,
      input
    });

    return res.json({ reply: response.output_text || "উত্তর পাওয়া যায়নি।" });
  } catch (error) {
    console.error("AI request failed:", error.status || error.name || "Unknown");
    return res.status(502).json({
      error: "AI service temporarily unavailable"
    });
  }
});

const port = Number(process.env.PORT) || 10000;
app.listen(port, "0.0.0.0", () => {
  console.log(`Cherry AI backend listening on port ${port}`);
});
