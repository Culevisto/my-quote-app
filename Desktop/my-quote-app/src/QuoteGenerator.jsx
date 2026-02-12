import React, { useState, useEffect, useRef } from 'react';

const QuoteGenerator = () => {
  const [quote, setQuote] = useState('Loading...');
  const [author, setAuthor] = useState('');
  const [isTyping, setIsTyping] = useState(false);
  const [isFading, setIsFading] = useState(false);
  const [copyText, setCopyText] = useState('Copy Quote');
  
  const typingTimeoutRef = useRef(null);

  const formatSentence = (text) => {
    if (!text) return "";
    let lower = text.toLowerCase();
    return lower.replace(/(^\w|[\.\!\?]\s*\w)/g, (c) => c.toUpperCase());
  };

  const typeText = (text, authorName) => {
    if (typingTimeoutRef.current) clearTimeout(typingTimeoutRef.current);
    
    setIsTyping(true);
    setQuote('');
    setAuthor('');
    
    let index = 0;
    const speed = 20;

    const type = () => {
      if (index < text.length) {
        setQuote((prev) => prev + text[index]);
        index++;
        typingTimeoutRef.current = setTimeout(type, speed);
      } else {
        setIsTyping(false);
        setAuthor(authorName);
      }
    };
    type();
  };

  const getQuote = async () => {
    if (isTyping) return;
    setIsFading(true);

    try {
      const response = await fetch('https://dummyjson.com/quotes/random');
      const data = await response.json();

      // Убеждаемся, что данные есть, прежде чем форматировать
      const qText = data.quote ? `"${formatSentence(data.quote)}"` : "No quote found";
      const aText = data.author ? `— ${data.author}` : "— Unknown";

      setTimeout(() => {
        setIsFading(false);
        typeText(qText, aText);
        localStorage.setItem("lastQuote", qText);
        localStorage.setItem("lastAuthor", aText);
      }, 300);

    } catch (error) {
      setQuote('Failed to load quote. Please try again.');
      setIsFading(false);
      setIsTyping(false);
    }
  };

  useEffect(() => {
    const savedQuote = localStorage.getItem("lastQuote");
    const savedAuthor = localStorage.getItem("lastAuthor");

    if (savedQuote && savedAuthor) {
      typeText(savedQuote, savedAuthor);
    } else {
      getQuote();
    }
    return () => clearTimeout(typingTimeoutRef.current);
  }, []);

  const handleCopy = () => {
    navigator.clipboard.writeText(`${quote} ${author}`);
    setCopyText('Copied!');
    setTimeout(() => setCopyText('Copy Quote'), 1200);
  };

  return (
    <div className="quote-app-container">
      <style>{`
        /* Сброс стандартных отступов Vite/Браузера */
        :global(body), :global(#root) {
          margin: 0 !important;
          padding: 0 !important;
          width: 100vw !important;
          max-width: 100% !important;
        }

        .quote-app-container {
          min-height: 100vh;
          width: 100vw;
          display: flex;
          justify-content: center;
          align-items: center;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          font-family: 'Arial', sans-serif; /* Вернул Arial */
          margin: 0;
          padding: 20px;
          box-sizing: border-box;
        }

        .container {
          width: 100%;
          max-width: 600px;
        }

        .quote-card {
          background: white;
          padding: clamp(30px, 10vw, 60px) clamp(20px, 5vw, 40px);
          border-radius: 15px;
          box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
          text-align: center;
          animation: slideIn 0.5s ease;
        }

        @keyframes slideIn {
          from { opacity: 0; transform: translateY(30px); }
          to { opacity: 1; transform: translateY(0); }
        }

        .quote {
          font-size: clamp(20px, 5vw, 24px);
          line-height: 1.6;
          color: #333;
          margin-bottom: 20px;
          min-height: 100px;
          display: flex;
          align-items: center;
          justify-content: center;
          transition: opacity 0.3s ease;
        }

        .author {
          font-size: 16px;
          color: #764ba2;
          font-style: italic;
          margin-bottom: 30px;
          transition: opacity 0.3s ease;
        }

        .fade { opacity: 0; }

        .btn-group {
          display: flex;
          gap: 15px;
          justify-content: center;
          flex-wrap: wrap; /* Чтобы кнопки не вылезали на мобилках */
        }

        .btn {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: white;
          border: none;
          padding: 12px 30px;
          font-size: 16px;
          border-radius: 50px;
          cursor: pointer;
          transition: transform 0.2s ease, box-shadow 0.2s ease;
          min-width: 160px;
        }

        .btn:hover:not(:disabled) {
          transform: translateY(-2px);
          box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
        }

        .btn:disabled {
          opacity: 0.6;
          cursor: not-allowed;
        }

        .copy-btn {
          background: #f1f2f6;
          color: #333;
        }

        @media (max-width: 480px) {
          .btn { width: 100%; }
        }
      `}</style>

      <div className="container">
        <div className="quote-card">
          <p className={`quote ${isFading ? 'fade' : ''}`}>
            {quote}
          </p>
          <p className={`author ${isFading ? 'fade' : ''}`}>
            {author}
          </p>
          
          <div className="btn-group">
            <button className="btn copy-btn" onClick={handleCopy} disabled={isTyping}>
              {copyText}
            </button>
            <button className="btn" onClick={getQuote} disabled={isTyping}>
              New Quote
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default QuoteGenerator;