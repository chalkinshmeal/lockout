package chalkinshmeal.lockout.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class Book {
    public static ItemStack createBook() {
        return new ItemStack(Material.WRITTEN_BOOK, 1);
    }

    public static ItemStack addText(ItemStack book, int pageIndex, String text, ChatColor color) {
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        String originalText = "";

        if (hasPage(book, pageIndex)) {
            originalText = bookMeta.getPage(pageIndex);
        }

        BaseComponent[] page = new ComponentBuilder(originalText).append(text).color(color.asBungee()).create();

        if (hasPage(book, pageIndex)) bookMeta.spigot().setPage(pageIndex, page);
        else bookMeta.spigot().addPage(page);

        book.setItemMeta(bookMeta);
        return book;
    }

    public static ItemStack setTitle(ItemStack book, String title) {
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setTitle(title);
        book.setItemMeta(bookMeta);
        return book;
    }

    public static ItemStack setAuthor(ItemStack book, String author) {
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setAuthor(author);
        book.setItemMeta(bookMeta);
        return book;
    }

    public static boolean hasPage(ItemStack book, int pageIndex) {
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        return (bookMeta.getPages().size() + 1 > pageIndex);
    }
}
